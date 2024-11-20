package vn.anpha.storage.File.DTO.Projection;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface FileProjection {
    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getFileId();

    String getName();

    String getDescription();

    boolean isHasPassword();

    Boolean getIsInDirectory();

    boolean isDirectory();

    boolean isDeleted();

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getParentFileId();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

    FileProjection getFileDetail();

    CreatorProjection getCreateBy();

    interface FileDetailProjection {
        Integer getSize();

        Boolean getIsUploaded();

    }

    interface CreatorProjection {
        @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
        UUID getUserId();

        String getEmail();
    }

}
