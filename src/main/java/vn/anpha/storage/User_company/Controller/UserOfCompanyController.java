package vn.anpha.storage.User_company.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
import vn.anpha.storage.User_company.Service.UserOfCompanyService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@RestController
@RequestMapping("/user_company")
// @RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfCompanyController {
    UserOfCompanyService userOfCompanyService;

    public UserOfCompanyController(UserOfCompanyService userOfCompanyService) {
        this.userOfCompanyService = userOfCompanyService;
    }

    @PostMapping("/add")
    public ApiResponseDto<UserOfCompany> addUserToCompany(@RequestBody addUserToCompanyRequestDto payload) {
        ApiResponseDto<UserOfCompany> response = new ApiResponseDto<>();
        response.setResult(this.userOfCompanyService.addUserToCompany(payload));
        return response;
    }

    @GetMapping("/get/{id}/employees")
    public ApiResponseDto<List<User>> getAllEmployee(@PathVariable() String id) {
        ApiResponseDto<List<User>> response = new ApiResponseDto<>();
        response.setResult(this.userOfCompanyService.getAllUserBelongCompany(id));
        return response;
    }

}
