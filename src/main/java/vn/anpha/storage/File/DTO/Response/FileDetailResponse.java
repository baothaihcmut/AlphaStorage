package vn.anpha.storage.File.DTO.Response;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDetailResponse {
    private UUID id;
    private Integer size;
    private String link;
    private Boolean isUploaded;
    private Boolean isVersion;

}
