package vn.anpha.storage.Option.DTO.Request;

import java.math.BigInteger;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionCreationRequest {
    private String optionId;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    private BigInteger price;

    @NotNull(message = "Value is required")
    private BigInteger value;

}
