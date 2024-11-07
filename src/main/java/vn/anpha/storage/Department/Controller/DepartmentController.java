package vn.anpha.storage.Department.Controller;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Service.CompanyService;
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
