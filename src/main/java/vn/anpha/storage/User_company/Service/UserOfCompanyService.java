package vn.anpha.storage.User_company.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.DTO.response.EmployeesResponseDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
//import vn.anpha.storage.User_company.Mapper.UserOfCompanyMapper;
import vn.anpha.storage.User_company.Mapper.UserOfCompanyMapper;
import vn.anpha.storage.User_company.Repository.UserCompanyRepository;
import vn.anpha.storage.User_company.Repository.UserCompanyResponseProjection;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfCompanyService {
    UserCompanyRepository userCompanyRepository;
    AuthoticationService authoticationService;
    UserRepository userRepository;
    CompanyRepository companyRepository;
    UserOfCompanyMapper userOfCompanyMapper;

    public User GetUserByEmail(String email) {

        List<User> users = this.userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        } else {
            return users.get(0);
        }
    }

    public Company getCompany(String uuid) {
        return companyRepository.findById(uuid).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
    }

    public ApiResponseDto<String> addUserToCompany(@NotNull addUserToCompanyRequestDto data) throws AppException {
        authoticationService.getUserByToken();

        User employee = this.GetUserByEmail(data.getEmployeeEmail());
        if (employee == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        Company company = this.getCompany(data.getCompanyId());
        if (company == null) {
            throw new AppException(ErrorCode.COMPANY_NOT_EXISTED);
        }
        try {
            log.info("before add");
            boolean exists = userCompanyRepository.existsByCompanyIdAndEmployeeId(data.getCompanyId(),
                    employee.getUserId()) > 0;
            if (exists) {
                throw new AppException(ErrorCode.USER_ALREADY_IN_COMPANY);
            }

            this.userCompanyRepository.insertUserToCompany(data.getCompanyId(), employee.getUserId());
            log.info("after add");
            return ApiResponseDto.<String>builder()
                    .message("success invite")
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }

    public List<User> getAllUserBelongCompany(String Id) {
        authoticationService.getUserByToken();

        List<UserOfCompany> userCompany = this.userCompanyRepository.findByCompany(Id);
        List<User> users = userCompany.stream()
                .map(UserOfCompany::getEmployee) // Lấy user từ từng đối tượng UserOfCompany
                .collect(Collectors.toList()); // Chuyển thành danh sách
        return users;
    }

    public String acceptInvite(String companyId) {
        User myInfo = authoticationService.getUserByToken();
        try {
            this.userCompanyRepository.acceptInviteFromCompany(companyId, myInfo.getUserId());
            return "Accept invite successfully";

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "Accept invite failed";

    }

    public Page<EmployeesResponseDto> getAllUserBelongCompany(String page, String limit, String companyId) {
        authoticationService.getUserByToken();

        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(limit);

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);


        Page<UserOfCompany> userCompanyPage = userCompanyRepository.findUserOfCompaniesByCompany(companyId, pageable);
        log.info(userCompanyPage.toString());

        // Convert UserOfCompany entities to EmployeesResponseDto using the helper method
        return changeFromUserToEmployeesResponseDto(userCompanyPage);
    }
    private Page<EmployeesResponseDto> changeFromUserToEmployeesResponseDto(Page<UserOfCompany> userOfCompanyPage) {
        List<EmployeesResponseDto> employeeResponseDtos = userOfCompanyPage.stream()
                .map(userOfCompany -> EmployeesResponseDto.builder()
                        .userId(userOfCompany.getEmployee().getUserId())
                        .email(userOfCompany.getEmployee().getEmail())
                        .fullName(userOfCompany.getEmployee().getFullName())
                        .address(userOfCompany.getEmployee().getAddress())
                        .phone(userOfCompany.getEmployee().getPhone())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(employeeResponseDtos, userOfCompanyPage.getPageable(), userOfCompanyPage.getTotalElements());
    }


}
