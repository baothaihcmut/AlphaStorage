package vn.anpha.storage.Storage.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VersionLinkDTO {
    private String link;
    private Integer size;
    private LocalDateTime createAt;
}
