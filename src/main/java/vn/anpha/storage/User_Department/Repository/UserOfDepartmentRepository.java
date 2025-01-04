package vn.anpha.storage.User_Department.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.User_Department.DTO.projection.DepartmentUserDto;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;
import vn.anpha.storage.User_Department.Entity.DepartmentUser.DepartmentUserId;

@Repository
public interface UserOfDepartmentRepository extends JpaRepository<DepartmentUser, DepartmentUserId> {
        @Query(value = """
                        SELECT *
                        FROM department_of_user
                        where user_id = :user_id And department_id=:department_id
                                                         """, nativeQuery = true)
        Optional<DepartmentUserDto> findUserOfDepartment(@Param("user_id") String user_id,
                        @Param("department_id") String department_id);

        @Query(value = """
                        SELECT *
                        FROM department_of_user
                        where user_id = :user_id And department_id=:department_id
                                                         """, nativeQuery = true)
        Optional<DepartmentUser> getEntityDepartmentUser(@Param("user_id") String user_id,
                        @Param("department_id") String department_id);

        @Query(value = "SELECT * "
                        + "FROM department_of_user "
                        + "where is_manager=true And department_id=:department_id", nativeQuery = true)
        List<DepartmentUserDto> findManagerOfDepartment(
                        @Param("department_id") String department_id);

        @Query(value = "SELECT count(department_id) "
                        + "FROM department_of_user "
                        + "where is_manager=true And department_id=:department_id and user_id=:user_id", nativeQuery = true)
        Long checkManagerDepartment(
                        @Param("department_id") String department_id, String user_id);

        @Query(value = "SELECT count(department_id) "
                        + "FROM department_of_user "
                        + "where department_id=:department_id and user_id=:user_id", nativeQuery = true)
        Long checkExistUserDepartment(
                        @Param("department_id") String department_id, String user_id);

        @Query(value = "SELECT  DU.department_id,DU.user_id,DU.is_manager,U.email,U.full_name " +
                        "FROM department_of_user as DU ,users as U "
                        + " where DU.department_id=:department_id And U.user_id=DU.user_id", nativeQuery = true)

        Page<UserDepartmentResponseProjection> findAllUserOfDepartment(@Param("department_id") String department_id,
                        Pageable pageable);

        @Modifying
        @Query(value = """
                        INSERT INTO department_of_user  (department_id, user_id, created_at, updated_at, is_manager)
                        VALUES (:departmentId, :userId, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :isManager)""", nativeQuery = true)
        void insertUserToDepartment(@Param("departmentId") String departmentId,
                        @Param("userId") String userId, @Param("isManager") Boolean isManager);

        @Modifying

        @Query(value = """
                        UPDATE department_of_user
                        SET is_manager = :isManager
                        WHERE department_id=:department_id and user_id=:user_id
                        """, nativeQuery = true)
        void updateUserOfCompany(@Param("department_id") String department_id, String user_id, boolean isManager);

}