package vn.anpha.storage.File.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceUploadDTO {
    private String description;
    private Boolean isUpdated;
}
