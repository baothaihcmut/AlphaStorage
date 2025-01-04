package vn.anpha.storage.File.DTO.Projection;

import java.time.LocalDateTime;

public interface FileDTO {
    String getFileId();

    String getName();

    String getDescription();

    Boolean getHasPassword();

    Boolean getIsInDirectory();

    Boolean getIsDirectory();

    Boolean getIsDeleted();

    Integer getTotalSize();

    String getDepartmentId();

    String getCreateUserId();

    String getParentFileId();

    LocalDateTime getCreateAt();

    LocalDateTime getUpdatedAt();

    LocalDateTime getDeletedAt();

}
