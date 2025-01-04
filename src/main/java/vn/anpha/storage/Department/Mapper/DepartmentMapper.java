package vn.anpha.storage.Department.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.Department.DTO.request.DepartmentCreationDTO;
import vn.anpha.storage.Department.Entity.Department;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DepartmentMapper {
    Department toDepartment(DepartmentCreationDTO company);

    // DepartmenResponse toDepartmentResponse(Department department);

}
