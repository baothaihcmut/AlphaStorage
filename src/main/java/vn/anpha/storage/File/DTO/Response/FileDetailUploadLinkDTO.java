package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import vn.anpha.storage.File.DTO.Projection.FileDetailDTO;

@Data
@AllArgsConstructor
public class FileDetailUploadLinkDTO {
    private FileDetailDTO fileDetail;
    private FileMetaDataLinkDTO uploadLinkInfo;

}
