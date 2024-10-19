package vn.anpha.storage.Company.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.response.CompanyCreationResponse;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.DTO.response.CompanyUpdateResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Mapper.CompanyMapper;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    AuthoticationService authoticationService;
    public CompanyCreationResponse createCompany(CompanyCreationRequest companyCreationRequest) {

        User user= authoticationService.getUserByToken(companyCreationRequest.getToken());
        if (companyRepository.existsCompanyByName(companyCreationRequest.getName())){
            throw new AppException(ErrorCode.COMPANY_EXISTED);
        }

        Company company = companyMapper.toCompany(companyCreationRequest);

        try {
            company.setCreateBy(user);
            company.setTotal_size(BigInteger.ZERO);
            company = companyRepository.save(company);
        }
        catch (Exception e){
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
        CompanyCreationResponse companyCreationResponse;
        companyCreationResponse=companyMapper.toCompanyCreationResponse(company);
        companyCreationResponse.setCreateBy(user);
        companyCreationResponse.setTotal_size(BigInteger.ZERO);
        return companyCreationResponse;
    }

    public CompanyUpdateResponse updateCompany(CompanyUpdateRequest request){

        User user= authoticationService.getUserByToken(request.getToken());
        if (!companyRepository.existsCompanyByName(request.getName())){
            throw new AppException(ErrorCode.COMPANY_NOT_EXISTED);
        }

        Company company = companyRepository.findCompanyByName(request.getName());

        if(!Objects.equals(user.getFullName(), company.getCreateBy().getFullName())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        switch (request.getOption()){
                case "option1":
                company.setTotal_size(company.getTotal_size().add(BigInteger.valueOf(10000))); //NEED TO RESET VALUE
                break;
                case "option2":
                company.setTotal_size(company.getTotal_size().add(BigInteger.valueOf(20000))); //NEED TO RESET VALUE
                break;
                case "option3":
                    company.setTotal_size(company.getTotal_size().add(BigInteger.valueOf(30000))); //NEED TO RESET VALUE
                    break;
                    default:
                        break;
        }

        try {

            company = companyRepository.save(company);
        }
        catch (Exception e){
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

        return companyMapper.toCompanyUpdateResponse(company);
    }

    public List<CompanyResponse> getCompany(String token){
        User user= authoticationService.getUserByToken(token);

        var companyList = companyRepository.findAllByCreateBy(user);
        log.info(user.toString());
        log.info(companyList.toString());

        return companyList.stream().map(companyMapper::toCompanyResponse).toList();

    }
}
