package vn.anpha.storage.Version.DTO.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionCreationDTO {
    private String versionId;
    private String link;
    private String description;
    private Integer size;
    private String fileId;
    private String updateUserId;
    private LocalDateTime createdAt;
}
