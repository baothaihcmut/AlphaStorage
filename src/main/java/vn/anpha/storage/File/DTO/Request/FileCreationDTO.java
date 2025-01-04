package vn.anpha.storage.File.DTO.Request;

import jakarta.validation.Valid;
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
    private String fileId;

    @NotBlank(message = "Name is required.")
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    private Boolean hasPassword;

    private String parentFileId;

    @NotNull(message = "Department ID is required.")
    private String departmentId;

    private String createUserId;

    @Size(max = 255, message = "Password must not exceed 255 characters.")
    private String password;

    @NotNull(message = "isInDirectory flag is required.")
    private Boolean isInDirectory;

    @NotNull(message = "isDirectory flag is required.")
    private Boolean isDirectory;

    private Integer totalSize;

    @Size(max = 10, min = 0, message = "You can associate up to 10 tags only.")
    private String[] tagIds;

    @Valid
    private FileDetailCreationDTO fileDetail;

}
