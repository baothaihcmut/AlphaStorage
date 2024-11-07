package vn.anpha.storage.User_Department.Controller;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Service.DepartmentService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

import org.springframework.web.bind.annotation.*;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;

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
