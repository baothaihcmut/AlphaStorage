package vn.anpha.storage.User_company.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.User.Entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class addUserToCompanyResponseDto {

    private User employee;
    private Company companyId;
}
