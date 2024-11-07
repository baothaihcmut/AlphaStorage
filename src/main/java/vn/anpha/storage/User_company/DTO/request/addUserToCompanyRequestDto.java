package vn.anpha.storage.User_company.DTO.request;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.User.Entity.User;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class addUserToCompanyRequestDto {

    private String employeeEmail;
    private UUID companyId;

}
