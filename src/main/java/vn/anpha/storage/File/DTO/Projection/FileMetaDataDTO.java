package vn.anpha.storage.File.DTO.Projection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface FileMetaDataDTO {

    String getFileId();

    Integer getSize();

    @JsonIgnore
    String getLink();

    Boolean getIsUploaded();

    Boolean getIsUploading();

    Boolean getIsVersion();

    @JsonIgnore
    String getBucketName();

}