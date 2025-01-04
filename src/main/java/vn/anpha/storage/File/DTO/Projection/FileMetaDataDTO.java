package vn.anpha.storage.File.DTO.Projection;

public interface FileMetaDataDTO {

    String getFileId();

    Integer getSize();

    String getLink();

    Boolean getIsUploaded();

    Boolean getIsUploading();

    Boolean getIsVersion();

    String getBucketName();

    String getMimeType();

}