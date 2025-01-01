package vn.anpha.storage.Company.DTO.response;

import java.math.BigInteger;
import java.util.UUID;

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

<<<<<<< HEAD
    private String companyId;
=======
    private String companyId ;
>>>>>>> 4b3c6d73d619b3459266c8447950306d59ffdd2c
    private String name;

    String createBy;

    private BigInteger total_size;

    private BigInteger limit_size;
}
