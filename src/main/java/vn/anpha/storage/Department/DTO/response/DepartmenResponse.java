package vn.anpha.storage.Department.DTO.response;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmenResponse {
    private UUID departmentId;

    private String name;

    private UUID companyid;

    private List<DepartmentUser> employees;

    private List<DepartmentUser> managers;

}
