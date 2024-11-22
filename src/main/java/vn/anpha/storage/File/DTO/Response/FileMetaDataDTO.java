package vn.anpha.storage.File.DTO.Response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vn.anpha.storage.common.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataDTO extends BaseDTO {
    @Getter(AccessLevel.NONE)
    byte[] fileId;
    Integer size;
    @JsonIgnore
    String link;
    Boolean isUploaded;
    boolean isVersion;
    @JsonIgnore
    String bucketName;

    public UUID getFileId() {
        return this.bytetoUuid(fileId);
    }
}
