package vn.anpha.storage.File.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpdateInfoDTO {

    @Size(max = 255, message = "Name must not exceed 255 characters.")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    private String description;

    private Boolean hasPassword;

    @Size(max = 255, message = "Password must not exceed 255 characters.")
    private String password;
}
