package vn.anpha.storage.File.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataLinkDTO {
    private FileMetaDataDTO metaData;
    private String downloadLink;
    private Integer duration;
}
