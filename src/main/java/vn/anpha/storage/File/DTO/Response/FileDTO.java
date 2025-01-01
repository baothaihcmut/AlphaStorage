package vn.anpha.storage.File.DTO.Response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.common.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDTO extends BaseDTO {
    @Getter(AccessLevel.NONE)
    private String fileId;

    private String name;

    private String description;

    private Boolean hasPassword;

    private Boolean isInDirectory;

    private Boolean isDirectory;

    private Boolean isDeleted;

    @Getter(AccessLevel.NONE)
    private String departmentId;

    @Getter(AccessLevel.NONE)
    private String createUserId;

    @Getter(AccessLevel.NONE)
    private String parentFileId;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public String getFileId() {
        return fileId;
    }

    public String getParentFileId() {
        return parentFileId;
    }

    public String getCreateUserId() {
        return createUserId ;
    }

    public String getDepartmentId() {
        return departmentId;
    }

}
