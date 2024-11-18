package vn.anpha.storage.File.DTO.Request;

import java.util.UUID;

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
public class FileCreationRequest {
    private String name;

    private String description;

    private Boolean hasPassword;

    private UUID fileId;

    private UUID departmentId;

    private String password;

    private Boolean isInDirectory;

    private Boolean isPersional;

    private Boolean isDirectory;

    private Boolean isPrivate;

    private UUID[] tagIds;

    private FileDetailCreationRequest fileDetail;

}
