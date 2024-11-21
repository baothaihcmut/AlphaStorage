package vn.anpha.storage.File.DTO.Projection;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface FileDetailProjection {
    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getId();

    Integer getSize();

    String getLink();

    Boolean getIsUploaded();

    String getBucketName();

    Boolean getIsVersion();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
