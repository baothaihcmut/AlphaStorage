package vn.anpha.storage.Version.DTO.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface VersionDetailDTO {

    String getVersionId();

    @JsonIgnore
    String getLink();

    String getDescription();

    Integer getSize();

    String getFileId();

    String getFileName();

    String getUpdateUserId();

    String getUpdateUserEmail();

    LocalDateTime getCreatedAt();

}
