package vn.anpha.storage.Version.DTO.response;

import java.time.LocalDateTime;
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
public class VersionDTO extends BaseDTO {
    @Getter(AccessLevel.NONE)
    private byte[] versionId;

    @JsonIgnore
    private String link;

    private String description;

    private Integer size;

    @Getter(AccessLevel.NONE)
    private byte[] fileId;

    @Getter(AccessLevel.NONE)
    private byte[] updateUserId;

    private LocalDateTime createdAt;

    public UUID getVersionId() {
        return this.bytetoUuid(versionId);
    }

    public UUID getFileId() {
        return this.bytetoUuid(fileId);
    }

    public UUID getUpdateUserId() {
        return this.bytetoUuid(updateUserId);
    }

}
