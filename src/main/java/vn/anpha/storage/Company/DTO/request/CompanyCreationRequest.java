package vn.anpha.storage.Company.DTO.request;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyCreationRequest {

    /*WE NEED PAYMENT DESCRIPTION IN NEXT VERSION*/

    private String name;

    private String description;

    private BigInteger limit_size;

}
