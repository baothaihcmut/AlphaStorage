package vn.anpha.storage.File.DTO.Response;

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
public class FileResponse {
    private UUID fileId;

    private String name;

    private String description;

    private Boolean hasPassword;

    private UUID ownerId;

    private UUID parentFileId;

    private UUID departmentId;

    private UUID[] tagIds;

    private FileDetailResponse fileDetail;
}
