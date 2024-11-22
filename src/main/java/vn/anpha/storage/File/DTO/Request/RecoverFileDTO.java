package vn.anpha.storage.File.DTO.Request;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoverFileDTO {
    @NotNull(message = "Recover directory is required")
    @NotEmpty(message = "Recover directory id is required")
    private UUID recoverDirectoryId;
}
