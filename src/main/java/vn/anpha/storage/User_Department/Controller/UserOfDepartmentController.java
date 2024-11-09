package vn.anpha.storage.User_Department.Controller;

//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.Service.DepartmentService;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfDepartmentController {
    DepartmentService departmentService;

    // @PostMapping("/create")
    // ApiResponseDto<DepartmenResponse> createCompany(
    // @RequestBody DepartmentCreateRequest request) {
    // return ApiResponseDto.<DepartmenResponse>builder()
    // .result(departmentService.createDepartment(request))
    // .build();
    // }

}
