package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataLinkDTO {
    private Action action;
    private FileDetailDTO metaData;
    private String url;
    private Integer duration;
}
