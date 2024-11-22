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
    private byte[] fileId;

    private String name;

    private String description;

    private Boolean hasPassword;

    private Boolean isInDirectory;

    private Boolean isDirectory;

    private Boolean isDeleted;

    @Getter(AccessLevel.NONE)
    private byte[] departmentId;

    @Getter(AccessLevel.NONE)
    private byte[] createUserId;

    @Getter(AccessLevel.NONE)
    private byte[] parentFileId;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public UUID getFileId() {
        return this.bytetoUuid(fileId);
    }

    public UUID getParentFileId() {
        return this.bytetoUuid(parentFileId);
    }

    public UUID getCreateUserId() {
        return this.bytetoUuid(createUserId);
    }

    public UUID getDepartmentId() {
        return this.bytetoUuid(departmentId);
    }

}
