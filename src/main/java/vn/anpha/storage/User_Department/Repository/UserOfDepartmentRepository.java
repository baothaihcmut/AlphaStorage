package vn.anpha.storage.User_Department.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;

@Repository
public interface UserOfDepartmentRepository extends JpaRepository<DepartmentUser, UUID> {
        @Query(value = "SELECT * FROM department_of_user"
                        + " where user_id = :user_id And department_id=:department_id ", nativeQuery = true)
        Optional<DepartmentUser> findUserOfDepartment(@Param("user_id") UUID user_id,
                        @Param("department_id") UUID department_id);

        @Query(value = "SELECT * "
                        + "FROM department_of_user "
                        + "where is_manager=true And department_id=:department_id And ", nativeQuery = true)
        List<DepartmentUser> findManagerOfDepartment(
                        @Param("department_id") UUID department_id);

        @Query(value = "SELECT DU.id, DU.department_id,DU.user_id,U.email,U.full_name " +
                        "FROM department_of_user as DU ,users as U "
                        + " where DU.department_id=:department_id And U.user_id=DU.user_id", nativeQuery = true)

        Page<UserDepartmentResponseProjection> findAllUserOfDepartment(@Param("department_id") UUID department_id,
                        Pageable pageable);

}