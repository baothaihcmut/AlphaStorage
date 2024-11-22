package vn.anpha.storage.File.DTO.Response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.anpha.storage.common.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDetailDTO extends BaseDTO {
    @Getter(AccessLevel.NONE)
    private byte[] fileId;

    private String name;

    private String description;

    private Boolean hasPassword;

    private Boolean isInDirectory;

    private Boolean isDirectory;

    private Boolean isDeleted;

    @Getter(AccessLevel.NONE)
    private byte[] parentFileId;

    // department field
    @Getter(AccessLevel.NONE)
    private byte[] departmentId;

    @Getter(AccessLevel.NONE)
    private String departmentName;

    // create user field
    @Getter(AccessLevel.NONE)
    private byte[] createUserId;

    @Getter(AccessLevel.NONE)
    private String createUserEmail;

    // file detail field
    @Getter(AccessLevel.NONE)
    private Integer fileDetailSize;

    @Getter(AccessLevel.NONE)
    private Boolean fileDetailIsUploaded;

    @Getter(AccessLevel.NONE)
    private Boolean fileDetailIsVersion;

    private LocalDateTime createAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Data
    @AllArgsConstructor
    public class CreateUser {
        private UUID userId;
        private String email;
    }

    @Data
    @AllArgsConstructor
    public class Department {
        private UUID departmentId;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public class FileDetail {
        private Integer size;
        private Boolean isUploaded;
        private Boolean isVersion;
    }

    public UUID getFileId() {
        return this.bytetoUuid(fileId);
    }

    public UUID getParentFileId() {
        return this.bytetoUuid(parentFileId);
    }

    public CreateUser getCreateUser() {
        return new CreateUser(this.bytetoUuid(createUserId), createUserEmail);
    }

    public Department getDepartment() {
        return new Department(this.bytetoUuid(departmentId), departmentName);
    }

    public FileDetail getFileDetail() {
        return new FileDetail(fileDetailSize, fileDetailIsUploaded, fileDetailIsVersion);
    }

}
