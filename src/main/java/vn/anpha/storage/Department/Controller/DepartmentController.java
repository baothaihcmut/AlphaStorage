package vn.anpha.storage.Department.Controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateRequest;
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

        @GetMapping("/{id}")
        ApiResponseDto<DepartmenResponse> getACompanyProperties(@PathVariable UUID id) {
                return ApiResponseDto.<DepartmenResponse>builder()
                                .result(departmentService.getDepartmentById(id))
                                .build();
        }

        @DeleteMapping("/delete/{id}")
        ApiResponseDto<Boolean> deleteUserOfDepartment(@PathVariable UUID id) {
                return ApiResponseDto.<Boolean>builder()
                                .result(departmentService.deleteDepartmentById(id))
                                .build();
        }

        @PostMapping("/update/{id}")
        ApiResponseDto<DepartmenResponse> updateNameDepartment(@PathVariable UUID id,
                        @RequestBody DepartmentUpdateRequest request) {
                return ApiResponseDto.<DepartmenResponse>builder()
                                .result(departmentService.updateDepartmentName(request))
                                .build();
        }

        // @GetMapping("/getAllDepartment")
        // public ApiResponseDto<PaginateResponseDto> getAllDepartment(
        // @RequestParam("current") Optional<String> currentOptional,
        // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        // int current = currentOptional.isPresent() ?
        // Integer.parseInt(currentOptional.get()) : 1;
        // int pageSize = pageSizeOptional.isPresent() ?
        // Integer.parseInt(pageSizeOptional.get()) : 10;
        // // Pageable pageable = PageRequest.of(current - 1, pageSize);

        // ApiResponseDto<PaginateResponseDto> response = new ApiResponseDto<>();
        // response.setResult(departmentService.GetAllDepartment(pageable));
        // return response;
        // }
}
