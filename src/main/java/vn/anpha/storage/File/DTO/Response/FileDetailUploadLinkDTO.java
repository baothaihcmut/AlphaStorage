package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDetailUploadLinkDTO {
    private FileDetailDTO fileDetail;
    private FileMetaDataLinkDTO uploadLinkInfo;

}
