package vn.anpha.storage.File_Tag.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileTagCreationRequest {
    @NotNull(message = "tagId is required")
    private String tagId;

    @NotNull(message = "fileId is required")
    private String fileId;
}
