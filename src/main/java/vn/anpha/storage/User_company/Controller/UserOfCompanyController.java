package vn.anpha.storage.User_company.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponseDto<String> addUserToCompany(@RequestBody addUserToCompanyRequestDto payload) {
        log.info("start here");
        return this.userOfCompanyService.addUserToCompany(payload);
    }

    @GetMapping("/get/{id}/employees")
    public ApiResponseDto<Page> getAllEmployee(@PathVariable() String id,
                                               @RequestParam(defaultValue = "1")String page,
                                               @RequestParam(defaultValue = "10")String limit
                                                     ) {

        ApiResponseDto<Page> response = new ApiResponseDto<>();
        response.setResult(this.userOfCompanyService.getAllUserBelongCompany(page,limit,id));
        return response;
    }

    @PostMapping("accept/{company_id}")
    public ApiResponseDto<String> acceptInvite(@PathVariable() String company_id) {
        return ApiResponseDto.<String>builder()
                .result(this.userOfCompanyService.acceptInvite(company_id))
                .message("Success")
                .build();
    }

}
