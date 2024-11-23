package vn.anpha.storage.File.DTO.Request;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileDTO {
    @NonNull
    private Integer newFileSize;
}
