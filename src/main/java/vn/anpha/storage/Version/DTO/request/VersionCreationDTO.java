package vn.anpha.storage.Version.DTO.request;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionCreationDTO {
    private UUID versionId;
    private String link;
    private String description;
    private Integer size;
    private UUID fileId;
    private UUID updateUserId;
    private LocalDateTime createdAt;
}
