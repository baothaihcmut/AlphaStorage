package vn.anpha.storage.User_company.DTO.response;

import jakarta.persistence.Column;
import lombok.*;
import vn.anpha.storage.User.Entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeesResponseDto {
    private String userId;
    private String email;
    private String fullName;
    private String address;
    private String phone;
}
