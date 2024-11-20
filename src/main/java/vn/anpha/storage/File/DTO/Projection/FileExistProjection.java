package vn.anpha.storage.File.DTO.Projection;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface FileExistProjection {
    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getFileId();

    String getName();

    String getDescription();

    Boolean getHasPassword();

    Boolean getIsInDirectory();

    Boolean getIsDirectory();

    Boolean getIsDeleted();

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getParentFileId();

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getDepartmentId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

}
