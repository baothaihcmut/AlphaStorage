package vn.anpha.storage.File.DTO.Response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Version.DTO.response.VersionDTO;
import vn.anpha.storage.common.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class FileMetaDataDTO extends BaseDTO {
    @NonNull
    @Getter(AccessLevel.NONE)
    byte[] fileId;

    @NonNull
    Integer size;

    @NonNull
    @JsonIgnore
    String link;

    @NonNull
    Boolean isUploaded;

    @NonNull
    Boolean isVersion;

    @NonNull
    @JsonIgnore
    String bucketName;

    private VersionDTO version;

    public UUID getFileId() {
        return this.bytetoUuid(fileId);
    }
}
