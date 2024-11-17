package vn.anpha.storage.User_Department.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.Department.Service.DepartmentService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.DTO.request.UserDepartmentCreate;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;
import vn.anpha.storage.User_Department.Service.UserOfDepartmentService;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/userOfDepartment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfDepartmentController {
    @PersistenceContext
    private EntityManager entityManager;
    UserOfDepartmentService userOfDepartmentService;
    DepartmentRepository departmentRepository;

    @PostMapping("/add")
    public ApiResponseDto<DepartmentUser> addUserToCompany(@RequestBody UserDepartmentCreate payload) {
        User user = entityManager.getReference(User.class, payload.getUserId());
        Department department = entityManager.getReference(Department.class, payload.getDepartmentId());
        ApiResponseDto<DepartmentUser> response = new ApiResponseDto<>();

        response.setResult(this.userOfDepartmentService.createUserOfDepartment(user, department));

        return response;
    }

    @PostMapping("allUsers")
    public DepartmentUser getAll(@RequestBody UserDepartmentCreate payload) {
        return this.userOfDepartmentService.findUserAndDepartment(payload.getUserId(), payload.getDepartmentId());
    }

}
