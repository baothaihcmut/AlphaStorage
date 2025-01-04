package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Projection.StorageDetailDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileContentResponse {
    FileMetaDataDTO file;
    StorageDetailDTO storageDetail;
}
