package vn.anpha.storage.File.DTO.Projection;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface FileProjection {
    // @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.fileId)}")
    byte[] getFileId();

    String getName();

    String getDescription();

    Boolean getHasPassword();

    Boolean getIsInDirectory();

    Boolean getIsDirectory();

    Boolean getIsDeleted();

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.parentFileId)}")
    UUID getParentFileId();

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getDepartmentId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    Integer getSize();

    Boolean getIsUploaded();

    String getEmail();

}
