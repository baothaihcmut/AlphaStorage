package vn.anpha.storage.File.DTO.Request;

import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpdateInfoRequest {
    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    private Boolean hasPassword;

    @Size(max = 255, message = "Password must not exceed 255 characters.")
    private String password;

    @Size(max = 10, min = 0, message = "You can associate up to 10 tags only.")
    private UUID[] tagIds;
}
