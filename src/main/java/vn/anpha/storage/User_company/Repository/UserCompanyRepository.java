package vn.anpha.storage.User_company.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.anpha.storage.User_company.Entity.UserOfCompany;
import vn.anpha.storage.User_company.Entity.UserOfCompany.UserOfCompanyId;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserOfCompany, UserOfCompanyId> {

        @Query(value = "SELECT * FROM users_of_company WHERE company_id=:id ", nativeQuery = true)
        List<UserOfCompany> findByCompany(String id);

        @Modifying
        @Transactional
        @Query(value = """
                        INSERT INTO users_of_company (company_id, employee_id, created_at, updated_at, status)
                        VALUES (:#{#companyId}, :#{#userId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false)""", nativeQuery = true)
        void insertUserToCompany(@Param("companyId") String companyId, @Param("userId") String userId);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE users_of_company
                        SET status = true
                        WHERE company_id = :companyId AND employee_id = :employeeId
                        """, nativeQuery = true)
        void acceptInviteFromCompany(String companyId, String employeeId);

        @Query(value = "SELECT COUNT(*) FROM users_of_company WHERE company_id = :companyId AND employee_id = :employeeId", nativeQuery = true)
        int existsByCompanyIdAndEmployeeId(@Param("companyId") String companyId,
                        @Param("employeeId") String employeeId);

}
