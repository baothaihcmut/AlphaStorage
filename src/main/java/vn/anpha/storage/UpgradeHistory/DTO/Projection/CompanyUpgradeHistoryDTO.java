package vn.anpha.storage.UpgradeHistory.DTO.Projection;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface CompanyUpgradeHistoryDTO {
    String getCompanyId();

    String getOptionId();

    LocalDateTime getTime();

    String getOptionName();

    String getOptionDescription();

    BigInteger getOptionPrice();

    BigInteger getOptionValue();
}
