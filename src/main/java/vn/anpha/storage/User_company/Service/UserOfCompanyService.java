package vn.anpha.storage.User_company.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Notification.DTO.request.NotificationRequestDto;
import vn.anpha.storage.Notification.Entity.NotificationStatus;
import vn.anpha.storage.Notification.Entity.NotificationType;
import vn.anpha.storage.Notification.Service.NotificationService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
//import vn.anpha.storage.User_company.Mapper.UserOfCompanyMapper;
import vn.anpha.storage.User_company.Repository.UserCompanyRepository;
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
    NotificationService notificationService;
    CompanyService  companyService;

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
        User Sender=authoticationService.getUserByToken();

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
            notificationService.createNotification(NotificationRequestDto.builder()
                            .title("INVITE TO COMPANY")
                            .type(NotificationType.INVITE)
                            .content("")
                            .recipientId(employee.getUserId())
                            .senderId(Sender.getUserId())
                            .status(NotificationStatus.UNREAD)
                    .build());
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

    public String acceptInvite(String companyId, String employeeId) {
        User myInfo = authoticationService.getUserByToken();
        User employee = this.GetUserByEmail(employeeId);
        if (employee == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (!myInfo.getUserId().equals(employee.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        try {
            this.userCompanyRepository.acceptInviteFromCompany(companyId, employeeId);


            notificationService.createNotification(NotificationRequestDto.builder()
                    .title("INVITE TO COMPANY")
                    .type(NotificationType.INVITE)
                    .content("")
                    .recipientId(companyService.findOwnerCompany(companyId).getUserId())
                    .senderId(myInfo.getUserId())
                    .status(NotificationStatus.UNREAD)
                    .build());
            return "Accept invite successfully";g

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "Accept invite failed";

    }
}
