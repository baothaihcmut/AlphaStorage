package vn.anpha.storage.UpgradeHistory.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.UpgradeHistory.DTO.Projection.CompanyUpgradeHistoryDTO;
import vn.anpha.storage.UpgradeHistory.DTO.Request.UpgradeHistoryCreationDTO;
import vn.anpha.storage.UpgradeHistory.Entity.UpgradeHistory;
import vn.anpha.storage.UpgradeHistory.Entity.UpgradeHistoryId;

public interface UpgradeHistoryRepository extends JpaRepository<UpgradeHistory, UpgradeHistoryId> {
    @Modifying
    @Query(value = """
            INSERT INTO upgrade_histories (company_id, option_id, time)
            VALUES (:#{#upgradeHistory.company.companyId}, :#{#upgradeHistory.option.optionId}, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertUpgradeHistory(@Param("upgradeHistory") UpgradeHistoryCreationDTO upgradeHistory);

    @Query(value = """
            SELECT
                uh.company_id as companyId,
                uh.option_id as optionId,
                uh.time as time
                o.name as optionName,
                o.value as optionValue,
                o.price as optionPrice,
                o.description as optionDescription,
            FROM upgrade_histories uh
            JOIN options o ON uh.option_id = o.option_id
            WHERE uh.company_id = :companyId
            """, nativeQuery = true)
    List<CompanyUpgradeHistoryDTO> findUpgradeHistoryByCompanyId(@Param("companyId") String companyId);

}
