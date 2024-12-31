package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataLinkDTO {
    private Action action;
    private FileMetaDataDTO metaData;
    private String url;
    private Integer duration;
}
