package vn.anpha.storage.Company.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Mapper.CompanyMapper;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    AuthoticationService authoticationService;

    public CompanyResponse createCompany(CompanyCreationRequest companyCreationRequest) {

        User user = authoticationService.getUserByToken();
        if (companyRepository.existsCompanyByName(companyCreationRequest.getName())) {
            throw new AppException(ErrorCode.COMPANY_EXISTED);
        }

        Company company = companyMapper.toCompany(companyCreationRequest);

        try {

            company.setTotal_size(BigInteger.ZERO);
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
        return companyMapper.toCompanyResponse(company);
    }

    public CompanyResponse updateCompany(CompanyUpdateRequest request) {

        User user = authoticationService.getUserByToken();
        if (!companyRepository.existsCompanyByName(request.getName())) {
            throw new AppException(ErrorCode.COMPANY_NOT_EXISTED);
        }

        Company company = companyRepository.findCompanyByName(request.getName());

        if (!Objects.equals(user.getEmail(), company.getCreateBy())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        switch (request.getOption()) {
            case "option1":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(10000))); // NEED TO RESET VALUE
                break;
            case "option2":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(20000))); // NEED TO RESET VALUE
                break;
            case "option3":
                company.setLimit_size(company.getLimit_size().add(BigInteger.valueOf(30000))); // NEED TO RESET VALUE
                break;
            default:
                throw new AppException(ErrorCode.THIS_TYPE_DOES_NOT_EXIST);
        }

        try {

            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

        return companyMapper.toCompanyResponse(company);
    }

    public List<CompanyResponse> getCompanies() {
        User user = authoticationService.getUserByToken();
        // log.info("In get companys service");
        var companyList = companyRepository.findAllByCreateBy(user.getEmail());
        // return companyList;
        return companyList.stream().map(companyMapper::toCompanyResponse).toList();

    }

    public CompanyResponse getCompany(UUID uuid) {
        User user = authoticationService.getUserByToken();
        return companyMapper.toCompanyResponse(companyRepository.findAllById(uuid));

    }
}
