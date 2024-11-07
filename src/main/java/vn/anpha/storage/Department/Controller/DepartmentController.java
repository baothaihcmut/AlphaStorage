package vn.anpha.storage.Department.Controller;

//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Service.DepartmentService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
        DepartmentService departmentService;

        @PostMapping("/create")
        ApiResponseDto<DepartmenResponse> createCompany(
                        @RequestBody DepartmentCreateRequest request) {
                return ApiResponseDto.<DepartmenResponse>builder()
                                .result(departmentService.createDepartment(request))
                                .build();
        }

        // @PostMapping("/update")
        // ApiResponseDto<CompanyResponse> updateCompany(
        // @RequestBody CompanyUpdateRequest request) {
        // return ApiResponseDto.<CompanyResponse>builder()
        // .result(companyService.updateCompany(request))
        // .build();
        // }

        // @GetMapping("/GetListCompany")
        // ApiResponseDto<List<CompanyResponse>> getCompanyProperties() {
        // return ApiResponseDto.<List<CompanyResponse>>builder()
        // .result(companyService.getCompanies())
        // .build();
        // }

        // @GetMapping("/GetCompany/{id}")
        // ApiResponseDto<CompanyResponse> getACompanyProperties(
        // @PathVariable UUID id) {
        // return ApiResponseDto.<CompanyResponse>builder()
        // .result(companyService.getCompany(id))
        // .build();
        // }
}
