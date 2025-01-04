package vn.anpha.storage.File.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileContentDTO {
    @NotNull(message = "New file size is required.")
    private Integer size;

    @NotNull(message = "New file type is required.")
    private String mimeType;

    private Boolean isUploaded;
    private Boolean isUploading;

}
