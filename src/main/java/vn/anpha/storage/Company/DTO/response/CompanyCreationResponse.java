package vn.anpha.storage.Company.DTO.response;

import java.math.BigInteger;

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
public class CompanyCreationResponse {

    private String companyId;
    private String name;

    String createBy;

    private BigInteger total_size;

    private BigInteger limit_size;
}
