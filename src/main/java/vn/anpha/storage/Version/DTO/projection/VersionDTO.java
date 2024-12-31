package vn.anpha.storage.Version.DTO.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface VersionDTO {

    String getVersionId();

    @JsonIgnore
    String getLink();

    String getDescription();

    Integer getSize();

    String getFileId();

    String getUpdateUserId();

    LocalDateTime getCreatedAt();

}
