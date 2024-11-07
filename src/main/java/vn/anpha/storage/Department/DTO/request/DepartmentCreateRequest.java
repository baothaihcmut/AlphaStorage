package vn.anpha.storage.Department.DTO.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentCreateRequest {
    private String name;

    private UUID companyid;

}
