package vn.anpha.storage.File.DTO.Request;

import java.util.UUID;

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
public class FileDetailCreationDTO {
    private UUID fileId;

    @NotNull(message = "File size is required")
    private Integer size;

    private String link;

    private Boolean isUploaded;

    private Boolean isVersion;

    private String bucketName;

}
