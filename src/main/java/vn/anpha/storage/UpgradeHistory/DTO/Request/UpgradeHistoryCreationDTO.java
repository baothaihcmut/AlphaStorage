package vn.anpha.storage.UpgradeHistory.DTO;

import jakarta.validation.constraints.NotNull;

public class UpgradeHistoryCreationDTO {
    @NotNull(message = "UpgradeHistoryId is required")
    private String companyId;

    @NotNull(message = "Option id is required")
    private String optionId;
}
