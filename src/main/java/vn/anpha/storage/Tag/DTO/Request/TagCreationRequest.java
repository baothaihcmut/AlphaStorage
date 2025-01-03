package vn.anpha.storage.Tag.DTO.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCreationRequest {

    private String tagId;
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "isCompanyTag is required")
    private Boolean isCompanyTag;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TagCompanyCreationRequest {
        private String tagId;

        @NotNull(message = "companyId is required")
        private String companyId;
    }

    @Valid
    private TagCompanyCreationRequest tagCompany;
}
