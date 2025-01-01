package vn.anpha.storage.Department.Controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.DTO.request.DepartmentCreationDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateDTO;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Service.DepartmentService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
        DepartmentService departmentService;

        // @PostMapping("/create")
        // ApiResponseDto<DepartmenResponse> createDepartment(
        // @RequestBody DepartmentCreationDTO request) {
        // return ApiResponseDto.<DepartmenResponse>builder()
        // .result(departmentService.createDepartment(request))
        // .build();
        // }

        // @GetMapping("/get/{id}")
        // ApiResponseDto<DepartmenResponse> getDepartmentById(@PathVariable UUID id) {
        // return ApiResponseDto.<DepartmenResponse>builder()
        // .result(departmentService.getDepartmentById(id))
        // .build();
        // }

        @DeleteMapping("/delete/{id}")
        ApiResponseDto<Boolean> deleteDepartmentById(@PathVariable UUID id) {
                return ApiResponseDto.<Boolean>builder()
                                .result(departmentService.deleteDepartmentById(id))
                                .build();
        }

        // @PostMapping("/update/{id}")
        // ApiResponseDto<DepartmenResponse> updateDepartmentName(@PathVariable UUID id,
        // @RequestBody DepartmentUpdateDTO request) {
        // return ApiResponseDto.<DepartmenResponse>builder()
        // .result(departmentService.updateDepartmentName(request))
        // .build();
        // }

        @GetMapping("/getAllDepartment/{companyId}")
        public ApiResponseDto<PaginateResponseDto> getAllDepartment(
                        @RequestParam("current") Optional<String> currentOptional,
                        @RequestParam("pageSize") Optional<String> pageSizeOptional,
                        @PathVariable String companyId) {

                int current = currentOptional.map(Integer::parseInt).orElse(1);
                int pageSize = pageSizeOptional.map(Integer::parseInt).orElse(10);
                Pageable pageable = PageRequest.of(current - 1, pageSize);

                ApiResponseDto<PaginateResponseDto> response = new ApiResponseDto<>();
                response.setResult(departmentService.GetAllDepartment(pageable, companyId));
                return response;
        }
}
