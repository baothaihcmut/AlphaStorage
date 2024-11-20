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
public class RecoverFileRequest {
    @NotNull(message = "Recover parent id is required")
    @NotEmpty(message = "Recover parent id is required")
    private UUID recoverParentFileId;
}
