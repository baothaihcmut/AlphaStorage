package vn.anpha.storage.User_Department.DTO.request;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDepartmentCreate {
    @NotEmpty(message = "User ID is required.")
    private String userId;
    @NotEmpty(message = "departmentId is required.")
    private String departmentId;
    @NotEmpty(message = "isManager is required.")
    private boolean isManager;
}
