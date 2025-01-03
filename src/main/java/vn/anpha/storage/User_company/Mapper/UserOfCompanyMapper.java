package vn.anpha.storage.User_company.Mapper;

import org.mapstruct.Mapper;
import vn.anpha.storage.User_company.DTO.response.EmployeesResponseDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

import java.util.List;
//import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
//import vn.anpha.storage.User_company.DTO.response.getEmployeesResponseDto;
//import vn.anpha.storage.User_company.Entity.UserOfCompany;
//
////import java.util.List;


@Mapper(componentModel =  "spring")
public interface UserOfCompanyMapper {
    EmployeesResponseDto mapUserOfCompany(UserOfCompany userOfCompany);

}
