package vn.anpha.storage.Folder.DTO.Request;

import java.math.BigInteger;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FolderCreationRequest {

    private String name;

    private String description;

    private BigInteger total_size;

    private BigInteger limit_size;

    private Boolean isPersonal;

    private UUID parentFolderId;

    private UUID departmentId;

}
