package vn.anpha.storage.User_company.DTO.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class addUserToCompanyRequestDto {

    private String employeeEmail;
    private UUID companyId;

}
