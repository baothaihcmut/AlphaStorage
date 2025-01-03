package vn.anpha.storage.Department.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.DTO.projection.TreeDepartment;
import vn.anpha.storage.Department.DTO.request.DepartmentCreationDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateDTO;
import vn.anpha.storage.Department.Service.DepartmentService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {
        DepartmentService departmentService;

        @PostMapping("/create")
        ApiResponseDto<DepartmentDTO> createDepartment(
                        @RequestBody DepartmentCreationDTO request) {
                return ApiResponseDto.<DepartmentDTO>builder()
                                .result(departmentService.createDepartment(request))
                                .build();
        }

        @GetMapping("/get/{id}")
        ApiResponseDto<DepartmentDTO> getDepartmentById(@PathVariable String id) {
                return ApiResponseDto.<DepartmentDTO>builder()
                                .result(departmentService.getDepartmentById(id))
                                .message("Get department by Id")
                                .success(true)
                                .build();
        }

        @DeleteMapping("/delete/{id}")
        ApiResponseDto<Boolean> deleteDepartmentById(@PathVariable String id) {
                return ApiResponseDto.<Boolean>builder()
                                .result(departmentService.deleteDepartmentById(id))
                                .message("Delete department")
                                .success(true)
                                .build();
        }

        @PatchMapping("/update/{id}")
        ApiResponseDto<DepartmentDTO> updateDepartmentName(@PathVariable String id,
                        @RequestBody DepartmentUpdateDTO request) {
                return ApiResponseDto.<DepartmentDTO>builder()

                                .result(departmentService.updateDepartment(id, request))
                                .message("Update department")
                                .success(true)
                                .build();
        }

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
                response.setSuccess(true);
                response.setMessage("Get all department of the company");
                return response;
        }

        @GetMapping("/employee/{employeeId}/company/{companyId}")
        ApiResponseDto<List<TreeDepartment>> getDepartmentOfEmployee(@PathVariable String employeeId,
                        @PathVariable String companyId) {
                return ApiResponseDto.<List<TreeDepartment>>builder()
                                .result(departmentService.getDepartmentOfEmployee(employeeId, companyId))
                                .message("Get department by Id")
                                .success(true)
                                .build();
        }
}
