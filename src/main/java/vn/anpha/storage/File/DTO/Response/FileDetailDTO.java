package vn.anpha.storage.File.DTO.Response;

import java.time.LocalDateTime;

public interface FileDetailDTO {
    public String getFileId();

    public String getName();

    public String getDescription();

    public Boolean getHasPassword();

    public Boolean getIsInDirectory();

    public Boolean getIsDirectory();

    public Boolean getIsDeleted();

    public String getParentFileId();

    // department field
    public String getDepartmentId();

    public String getDepartmentName();

    // create user field
    public String getCreateUserId();

    public String getCreateUserEmail();

    // file detail field
    public Integer getFileDetailSize();

    public Boolean getFileDetailIsUploaded();

    public Boolean getFileDetailIsUploading();

    public Boolean getFileDetailIsVersion();

    public LocalDateTime getCreateAt();

    public LocalDateTime getUpdatedAt();

    public LocalDateTime getDeletedAt();

}
