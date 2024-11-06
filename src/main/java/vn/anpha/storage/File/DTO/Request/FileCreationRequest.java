package vn.anpha.storage.File.DTO.Request;

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
public class FileCreationRequest {
    private String name;

    private String description;

    private Boolean hasPassword;

    private String password;

    private String link;

    private Integer fileSize;

    private UUID folderId;
}
