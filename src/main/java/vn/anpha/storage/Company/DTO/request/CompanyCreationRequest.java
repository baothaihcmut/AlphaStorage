package vn.anpha.storage.Company.DTO.request;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
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
public class CompanyCreationRequest {

    /* WE NEED PAYMENT DESCRIPTION IN NEXT VERSION */

    private String name;

    private String description;

    private BigInteger limit_size;

    @NotNull(message = "has version is required")
    private Boolean hasVersion;

}
