package vn.anpha.storage.File.DTO.Projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageDetailDTO {
    private Action action;
    private String presignUrl;
    private String mimeType;
    private Integer size;
    private Integer duration;
}