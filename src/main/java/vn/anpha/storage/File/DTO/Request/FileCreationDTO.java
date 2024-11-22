package vn.anpha.storage.File.DTO.Request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class FileCreationDTO {
    private UUID fileId;

    @NotBlank(message = "Name is required.")
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    private Boolean hasPassword;

    private UUID parentFileId;

    @NotNull(message = "Department ID is required.")
    private UUID departmentId;

    private UUID createUserId;

    @Size(max = 255, message = "Password must not exceed 255 characters.")
    private String password;

    @NotNull(message = "isInDirectory flag is required.")
    private Boolean isInDirectory;

    @NotNull(message = "isDirectory flag is required.")
    private Boolean isDirectory;

    @Size(max = 10, min = 0, message = "You can associate up to 10 tags only.")
    private UUID[] tagIds;

    private FileDetailCreationDTO fileDetail;

}
