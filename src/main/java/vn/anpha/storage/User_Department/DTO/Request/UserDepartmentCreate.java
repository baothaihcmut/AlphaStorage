package vn.anpha.storage.User_Department.DTO.Request;

import org.checkerframework.common.value.qual.BoolVal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "isManager is required.")

    private Boolean isManager;
}
