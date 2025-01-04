package vn.anpha.storage.User_Department.Controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.User_Department.DTO.Request.UserDepartmentCreate;
import vn.anpha.storage.User_Department.DTO.Request.UserDepartmentUpdate;
import vn.anpha.storage.User_Department.DTO.projection.DepartmentUserDto;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;
import vn.anpha.storage.User_Department.Service.UserOfDepartmentService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/userOfDepartment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfDepartmentController {
    @PersistenceContext
    private EntityManager entityManager;
    UserOfDepartmentService userOfDepartmentService;
    DepartmentRepository departmentRepository;
    UserRepository userRepository;

    @PostMapping("/add")
    public ApiResponseDto<DepartmentUserDto> addUserDepartment(@Valid @RequestBody UserDepartmentCreate payload) {

        ApiResponseDto<DepartmentUserDto> response = new ApiResponseDto<>();
        response.setResult(this.userOfDepartmentService.createUserOfDepartment(payload));
        response.setMessage("Created user of department");
        response.setSuccess(true);
        return response;
    }

    @PostMapping("getByUserIdAndDepartmentId")
    public ApiResponseDto<DepartmentUserDto> getAll(@RequestBody @Valid UserDepartmentCreate payload) {
        ApiResponseDto<DepartmentUserDto> response = new ApiResponseDto<>();
        response.setResult(
                this.userOfDepartmentService.findUserAndDepartment(payload.getUserId(), payload.getDepartmentId()));
        return response;
    }

    @PatchMapping("update")
    public ApiResponseDto<DepartmentUserDto> updateUserOfDepartment(@RequestBody UserDepartmentUpdate payload) {

        // implement update logic here

        ApiResponseDto<DepartmentUserDto> response = new ApiResponseDto<>();

        response.setResult(this.userOfDepartmentService.updateUserOfDepartment(payload));

        return response;
    }

    @DeleteMapping("/delete")
    ApiResponseDto<Boolean> deleteUserOfDepartment(@PathVariable UserDepartmentCreate payload) {
        userOfDepartmentService.deleteUserOfDepartmentById(payload.getUserId(),
                payload.getDepartmentId());
        return ApiResponseDto.<Boolean>builder()

                .result(true)
                .build();
    }

    @GetMapping("/getAllUserOfDepartment/{departmentId}")
    public ApiResponseDto<PaginateResponseDto> getAllDepartment(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional,
            @PathVariable String departmentId) {

        int current = currentOptional.map(Integer::parseInt).orElse(1);
        int pageSize = pageSizeOptional.map(Integer::parseInt).orElse(10);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        ApiResponseDto<PaginateResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userOfDepartmentService.GetAllUser(pageable, departmentId));
        return response;
    }

}
