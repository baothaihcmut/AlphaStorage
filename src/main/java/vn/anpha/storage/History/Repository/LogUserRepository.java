package vn.anpha.storage.History.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.anpha.storage.History.Entity.LogUser;

@Repository
public interface LogUserRepository extends JpaRepository<LogUser, String> {

    @Query(value = """
            SELECT lu.*
            FROM log_users lu
            WHERE (:status IS NULL OR lu.status = :status)
              AND (:email IS NULL OR lu.email = :email)
              AND (:date IS NULL OR DATE(lu.dateUse) = :date)
            """,
            countQuery = """
            SELECT count(*) 
            FROM log_users lu
            WHERE (:status IS NULL OR lu.status = :status)
              AND (:email IS NULL OR lu.email = :email)
              AND (:date IS NULL OR DATE(lu.dateUse) = :date)
            """,
            nativeQuery = true)
    Page<LogUserResponseProjection> findFilter(
            Pageable pageable,
            String email,
            String status,
            String date);
}