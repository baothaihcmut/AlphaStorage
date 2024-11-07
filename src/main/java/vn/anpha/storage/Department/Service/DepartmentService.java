package vn.anpha.storage.Department.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Mapper.DepartmentMapper;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;
    AuthoticationService authoticationService;

    public DepartmenResponse createDepartment(DepartmentCreateRequest departmentCreateRequest) {
        User user = authoticationService.getUserByToken();
        // if
        // (departmentRepository.existsDepartmentByName(departmentCreateRequest.getName()))
        // {
        // throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
        // }
        Department department = departmentMapper.toDepartment(departmentCreateRequest);
        DepartmenResponse response = departmentMapper.toDepartmentResponse(department);
        return response;
    }
}
