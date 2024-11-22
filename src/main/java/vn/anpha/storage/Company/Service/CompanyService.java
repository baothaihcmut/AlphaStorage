package vn.anpha.storage.Company.Service;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.DTO.projections.CompanySizeProjection;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.request.UpGradeCompanyRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Mapper.CompanyMapper;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    AuthoticationService authoticationService;
    StorageService storageService;

    @Transactional
    public CompanyResponse createCompany(CompanyCreationRequest companyCreationRequest) {

        if (companyRepository.existsCompanyByName(companyCreationRequest.getName()) != 0) {
            throw new AppException(ErrorCode.COMPANY_EXISTED);
        }

        Company company = companyMapper.toCompany(companyCreationRequest);

        try {
            company.setTotal_size(BigInteger.ZERO);
            company = companyRepository.save(company);
            // create company bucket
            this.storageService.createBucket(company.getCompanyId().toString(), false);
        } catch (Exception e) {
            System.out.println(e);
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

        return companyMapper.toCompanyResponse(company);
    }

    public CompanyResponse updateCompany(UUID id, CompanyUpdateRequest request) {

        User user = authoticationService.getUserByToken();
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));

        if (!Objects.equals(user.getEmail(), company.getCreateBy())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (request.getName() != null && request.getName().length() > 0) {
            company.setName(request.getName());
        }
        if (request.getDescription() != null && request.getDescription().length() > 0) {
            company.setDescription(request.getDescription());
        }

        company = companyRepository.save(company);
        return companyMapper.toCompanyResponse(company);

    }

    public CompanyResponse updateGradeCompany(UUID id, UpGradeCompanyRequest request) {
        User user = authoticationService.getUserByToken();
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));

        if (!Objects.equals(user.getEmail(), company.getCreateBy())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        switch (request.getOption()) {
            case "option1":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(10000)));
                // NEED TO RESET VALUE
                break;
            case "option2":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(20000)));
                // NEED TO RESET VALUE
                break;
            case "option3":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(30000)));
                // NEED TO RESET VALUE
                break;
            default:
                throw new AppException(ErrorCode.THIS_TYPE_DOES_NOT_EXIST);
        }
        return companyMapper.toCompanyResponse(company);
    }

    public PaginateResponseDto<CompanyResponse> getCompanies(Pageable pageable) {
        User user = authoticationService.getUserByToken();

        Page<Company> companyList = companyRepository.findAllByCreateBy(user.getEmail(), pageable);
        var companys = companyList.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(companyList.getNumber())
                .PageSize(companyList.getSize())
                .TotalItems(companyList.getTotalElements())
                .TotalPages(companyList.getTotalPages())
                .build();
        PaginateResponseDto<CompanyResponse> responseDto = new PaginateResponseDto<CompanyResponse>();
        responseDto.setData(companys.stream().map(companyMapper::toCompanyResponse).toList());
        responseDto.setMetaPaginate(pageMeta);
        return responseDto;

    }

    public CompanyResponse getCompany(UUID uuid) {

        Company company = companyRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        return companyMapper.toCompanyResponse(company);
    }

    public boolean deleteCompanyById(UUID id) {
        checkOwnCompany(authoticationService.getUserByToken(), id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        this.companyRepository.deleteById(id);
        return true;

    }

    public Company checkOwnCompany(User user, UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        if (user.getEmail().equals(company.getCreateBy())) {
            return company;
        } else {
            throw new AppException(ErrorCode.USER_NOT_OWNCOMPANY);

        }
    }

    @Transactional
    public void createNewFileCompanySize(UUID companyId, Integer iaddtionSize) {
        CompanySizeProjection companySize = this.companyRepository.findCompanyNameAndSize(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        BigInteger addtionSize = BigInteger.valueOf(iaddtionSize.longValue());
        if (companySize.getTotalSize().add(addtionSize).compareTo(companySize.getLimitSize()) == 1) {
            throw new AppException(ErrorCode.COMPANY_EXEED_LIMIT_SIZE);
        }
        BigInteger newSize = companySize.getTotalSize().add(addtionSize);
        this.companyRepository.updateCompanySize(companyId, newSize);
    }

    @Transactional
    public void updateFileCompanySize(UUID companyId, Integer oldSize, Integer newSize) {
        CompanySizeProjection company = this.companyRepository.findCompanyNameAndSize(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        BigInteger newSizeBig = BigInteger.valueOf(newSize.longValue());
        BigInteger oldSizeBig = BigInteger.valueOf(oldSize.longValue());
        BigInteger newSizeOfCompany = company.getTotalSize().subtract(oldSizeBig).add(newSizeBig);
        if (newSizeOfCompany.compareTo(company.getLimitSize()) == 1) {
            throw new AppException(ErrorCode.COMPANY_EXEED_LIMIT_SIZE);
        }
        this.companyRepository.updateCompanySize(companyId, newSizeOfCompany);
    }

    @Transactional
    public void removeFileCompany(UUID companyId, Integer size) {
        CompanySizeProjection company = this.companyRepository.findCompanyNameAndSize(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        BigInteger newSize = company.getTotalSize().subtract(BigInteger.valueOf(size.longValue()));
        this.companyRepository.updateCompanySize(companyId, newSize);
    }
}
