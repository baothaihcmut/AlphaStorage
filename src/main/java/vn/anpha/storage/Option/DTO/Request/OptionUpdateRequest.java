package vn.anpha.storage.Option.DTO.Request;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionUpdateRequest {
    private String name;

    private String description;

    private BigInteger price;

    private BigInteger value;
}
