package vn.anpha.storage.Company.DTO.response;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    private UUID companyId;

    private String name;

    private String description;

    private BigInteger total_size;
    private BigInteger limit_size;

    private String createBy;

    private Set<UserOfCompany> userOfCompanys;

    private List<Department> departments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
