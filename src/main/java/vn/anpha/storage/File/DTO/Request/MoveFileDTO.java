package vn.anpha.storage.File.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveFileDTO {

    @NotNull(message = "New Directory Id is required")
    private String newDirectoryId;
}
