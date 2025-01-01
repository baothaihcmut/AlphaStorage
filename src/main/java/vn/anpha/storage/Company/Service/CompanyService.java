package vn.anpha.storage.Company.Service;

import java.math.BigInteger;
import java.util.Optional;
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
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.request.UpGradeCompanyRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Mapper.CompanyMapper;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.Company.interfaceCompany.CompanyInterface;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserDepartmentResponseProjection;
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
    public CompanyInterface createCompany(CompanyCreationRequest companyCreationRequest) {

        try {
            String companyId = UUID.randomUUID().toString();
            String OwnerId = authoticationService.GetUserIdByToken();
            companyRepository.insertCompany(companyCreationRequest, companyId, OwnerId);

            // create company bucket
            // this.storageService.createBucket(companyId,
            // companyCreationRequest.getHasVersion());
            return this.companyRepository.findCompanyById(companyId)
                    .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        } catch (Exception e) {
            System.out.println(e);
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

    }

    @Transactional
    public CompanyInterface updateCompanyInfo(String companyId, CompanyUpdateRequest request) {

        String OwnerId = authoticationService.GetUserIdByToken();
        this.companyRepository.updateCompanyInfo(companyId, OwnerId, request.getName(), request.getDescription());
        return this.companyRepository.findCompanyById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));

    }

    @Transactional
    public CompanyInterface updateGradeCompany(String companyId, UpGradeCompanyRequest request) {
        // BigInteger oldSize = this.companyRepository.f(companyId);

        Optional<CompanyInterface> company = this.companyRepository.findCompanyById(companyId);
        if (!company.isPresent()) {
            throw new AppException(ErrorCode.COMPANY_NOT_EXISTED);
        }

        BigInteger newSize = company.get().getLimitSize();
        String OwnerId = authoticationService.GetUserIdByToken();

        // Cập nhật kích thước mới dựa trên lựa chọn
        switch (request.getOption()) {
            case "option1":
                newSize = newSize.add(BigInteger.valueOf(10000)); // Cộng thêm 10000
                break;
            case "option2":
                newSize = newSize.add(BigInteger.valueOf(20000)); // Cộng thêm 20000
                break;
            case "option3":
                newSize = newSize.add(BigInteger.valueOf(30000)); // Cộng thêm 30000
                break;
            default:
                throw new AppException(ErrorCode.THIS_TYPE_DOES_NOT_EXIST);
        }

        try {

            // Cập nhật kích thước công ty trong cơ sở dữ liệu
            this.companyRepository.updateCompanySize(companyId, newSize, OwnerId);

            // Trả về công ty đã cập nhật
            return this.companyRepository.findCompanyById(companyId)
                    .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));

        } catch (Exception e) {
            // Xử lý ngoại lệ
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

    }

    public PaginateResponseDto<CompanyInterface> getCompaniesOwn(Pageable pageable) {
        String OwnerId = authoticationService.GetUserIdByToken();
        Page<CompanyInterface> pageUser = companyRepository.findAllByCreateBy(OwnerId, pageable);
        var userOfDepartment = pageUser.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageUser.getNumber())
                .PageSize(pageUser.getSize())
                .TotalItems(pageUser.getTotalElements())
                .TotalPages(pageUser.getTotalPages())
                .build();
        PaginateResponseDto<CompanyInterface> responseDto = new PaginateResponseDto<CompanyInterface>();
        responseDto.setData(userOfDepartment);
        responseDto.setMetaPaginate(pageMeta);

        return responseDto;

    }

    public CompanyInterface getCompany(String uuid) {

        try {

            return companyRepository.findCompanyById(uuid)
                    .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        } catch (Exception e) {
            // TODO: handle exception
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

    }

    public boolean deleteCompanyById(String id) {
        checkOwnCompany(authoticationService.getUserByToken(), id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        this.companyRepository.deleteById(id);
        return true;

    }

    public boolean checkOwnCompany(User user, String companyId) {
        Long count = companyRepository.CheckOwnCompany(companyId, authoticationService.GetUserIdByToken());
        return count > 0; // Nếu số lượng > 0, trả về true, nếu không thì false
    }

    // @Transactional
    // public CompanySizeProjection createNewFileCompanySize(UUID companyId, Integer
    // iaddtionSize) {
    // CompanySizeProjection companySize =
    // this.companyRepository.findCompanyNameAndSize(companyId)
    // .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
    // BigInteger addtionSize = BigInteger.valueOf(iaddtionSize.longValue());
    // if
    // (companySize.getTotalSize().add(addtionSize).compareTo(companySize.getLimitSize())
    // == 1) {
    // throw new AppException(ErrorCode.COMPANY_EXEED_LIMIT_SIZE);
    // }
    // BigInteger newSize = companySize.getTotalSize().add(addtionSize);
    // this.companyRepository.updateCompanySize(companyId, newSize);
    // return companySize;
    // }

    // @Transactional
    // public void updateFileCompanySize(UUID companyId, Integer oldSize, Integer
    // newSize, boolean isVersion,
    // Integer deletedVersionSize) {
    // CompanySizeProjection company =
    // this.companyRepository.findCompanyNameAndSize(companyId)
    // .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
    // BigInteger newSizeOfCompany;
    // if (isVersion) {
    // newSizeOfCompany =
    // company.getTotalSize().add(BigInteger.valueOf(newSize.longValue()))
    // .subtract(BigInteger.valueOf(deletedVersionSize.longValue()));
    // } else {
    // newSizeOfCompany =
    // company.getTotalSize().add(BigInteger.valueOf(newSize.longValue()))
    // .subtract(BigInteger.valueOf(oldSize.longValue()));
    // }
    // if (newSizeOfCompany.compareTo(company.getLimitSize()) == 1) {
    // throw new AppException(ErrorCode.COMPANY_EXEED_LIMIT_SIZE);
    // }
    // this.companyRepository.updateCompanySize(companyId, newSizeOfCompany);
    // }

    // @Transactional
    // public void removeFileCompany(UUID companyId, Integer size) {
    // CompanySizeProjection company =
    // this.companyRepository.findCompanyNameAndSize(companyId)
    // .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
    // BigInteger newSize =
    // company.getTotalSize().subtract(BigInteger.valueOf(size.longValue()));
    // this.companyRepository.updateCompanySize(companyId, newSize);
    // }
}
