package vn.anpha.storage.File_Tag.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileTagCreationRequest {
    private String tagId;
    private String fileId;
}
