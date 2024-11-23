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
public class VersionDetailDTO extends BaseDTO {
    @Getter(AccessLevel.NONE)
    private byte[] versionId;

    @JsonIgnore
    private String link;

    private String description;

    private Integer size;

    @Getter(AccessLevel.NONE)
    private byte[] fileId;

    @Getter(AccessLevel.NONE)
    private String fileName;

    @Getter(AccessLevel.NONE)
    private byte[] updateUserId;

    @Getter(AccessLevel.NONE)
    private String updateUserEmail;

    private LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    public class UpdateVersionUser {
        private UUID userid;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public class FileDetail {
        private UUID fileId;
        private String name;
    }

    public UUID getVersionId() {
        return this.bytetoUuid(versionId);
    }

    public FileDetail getFileDetail() {
        return new FileDetail(this.bytetoUuid(fileId), fileName);
    }

    public UpdateVersionUser getUpdateUser() {
        return new UpdateVersionUser(this.bytetoUuid(updateUserId), updateUserEmail);
    }
}
