package vn.anpha.storage.Company.DTO.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.User.Entity.User;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyCreationResponse {

    private String name;

    String createBy;

    private BigInteger total_size;

    private BigInteger limit_size;
}
