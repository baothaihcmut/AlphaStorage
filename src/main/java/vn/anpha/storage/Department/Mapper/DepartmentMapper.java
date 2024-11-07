package vn.anpha.storage.Department.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.DTO.response.CompanyUpdateResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Entity.User;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentCreateRequest company);

    DepartmenResponse toDepartmentResponse(Department department);
}
