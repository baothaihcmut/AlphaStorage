package vn.anpha.storage.User_company.DTO.response;

import lombok.*;
import vn.anpha.storage.User.Entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class getEmployeesResponseDto {
    private  List<User> users;
}
