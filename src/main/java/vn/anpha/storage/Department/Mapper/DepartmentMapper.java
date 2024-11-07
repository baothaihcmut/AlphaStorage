package vn.anpha.storage.Department.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentCreateRequest company);

    DepartmenResponse toDepartmentResponse(Department department);

}
