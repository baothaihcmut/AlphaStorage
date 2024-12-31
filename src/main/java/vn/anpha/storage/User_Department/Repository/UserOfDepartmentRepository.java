package vn.anpha.storage.User_Department.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.User_Department.Entity.DepartmentUser;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

@Repository
public interface UserOfDepartmentRepository extends JpaRepository<DepartmentUser, String> {
        @Query(value = "SELECT * FROM department_of_user"
                        + " where user_id = :user_id And department_id=:department_id ", nativeQuery = true)
        Optional<DepartmentUser> findUserOfDepartment(@Param("user_id") String user_id,
                        @Param("department_id") String department_id);

        @Query(value = "SELECT * "
                        + "FROM department_of_user "
                        + "where is_manager=true And department_id=:department_id", nativeQuery = true)
        List<DepartmentUser> findManagerOfDepartment(
                        @Param("department_id") String department_id);

        @Query(value = "SELECT DU.id, DU.department_id,DU.user_id,U.email,U.full_name " +
                        "FROM department_of_user as DU ,users as U "
                        + " where DU.department_id=:department_id And U.user_id=DU.user_id", nativeQuery = true)

        Page<UserDepartmentResponseProjection> findAllUserOfDepartment(@Param("department_id") String department_id,
                        Pageable pageable);

        @Modifying
        @Transactional
        @Query(value = """
                        INSERT INTO department_of_user  (department_id, user_id, create_at, update_at, is_manager)
                        VALUES (:departmentId, :userId, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :isManager)""", nativeQuery = true)
        DepartmentUser insertUserToDepartment(@Param("departmentId") String departmentId, @Param("userId")String userId, @Param("isManager")Boolean isManager);

}