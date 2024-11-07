package vn.anpha.storage.User_company.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
import vn.anpha.storage.User_company.Service.UserOfCompanyService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/getAllEmployee/{id}")
    public ApiResponseDto<List<User>> getAllEmployee(@PathVariable UUID id) {
        ApiResponseDto<List<User>> response = new ApiResponseDto<>();
        response.setResult(this.userOfCompanyService.getAllUserBelongCompany(id));
        return response;
    }

}
