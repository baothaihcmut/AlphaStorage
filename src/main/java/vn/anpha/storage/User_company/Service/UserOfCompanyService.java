package vn.anpha.storage.User_company.Service;

import java.util.List;
import java.util.UUID;
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
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
//import vn.anpha.storage.User_company.Mapper.UserOfCompanyMapper;
import vn.anpha.storage.User_company.Repository.UserCompanyRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfCompanyService {
    UserCompanyRepository userCompanyRepository;
    AuthoticationService authoticationService;
    UserRepository userRepository;
    CompanyRepository companyRepository;

    public User GetUserByEmail(String email) {

        List<User> users = this.userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        } else {
            return users.get(0);
        }
    }

    public Company getCompany(UUID uuid) {
        return companyRepository.findById(uuid).orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
    }

    public UserOfCompany addUserToCompany(@NotNull addUserToCompanyRequestDto data) {
        User user = authoticationService.getUserByToken();

        User employee = this.GetUserByEmail(data.getEmployeeEmail());
        Company company = this.getCompany(data.getCompanyId());

        UserOfCompany userCompany = new UserOfCompany();
        userCompany.setCompany(company);
        userCompany.setEmployee(employee);

        try {
            this.userCompanyRepository.save(userCompany);
            return userCompany;
        }

        catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }

    public List<User> getAllUserBelongCompany(UUID uuid) {
        User user = authoticationService.getUserByToken();
        Company company = this.getCompany(uuid);

        List<UserOfCompany> userCompany = this.userCompanyRepository.findByCompany(company);
        List<User> users = userCompany.stream()
                .map(UserOfCompany::getEmployee) // Lấy user từ từng đối tượng UserOfCompany
                .collect(Collectors.toList()); // Chuyển thành danh sách
        return users;
    }
}
