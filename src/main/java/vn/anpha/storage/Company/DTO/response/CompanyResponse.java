package vn.anpha.storage.Company.DTO.response;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    private String companyId;

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
