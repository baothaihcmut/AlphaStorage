package vn.anpha.storage.User_Department.Repository;

import java.util.List;
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
        DepartmentUser findUserOfDepartment(@Param("user_id") UUID user_id,
                        @Param("department_id") UUID department_id);

        @Query(value = "SELECT * "
                        + "FROM department_of_user "
                        + "where is_manager=true And department_id=:department_id And ", nativeQuery = true)
        List<DepartmentUser> findManagerOfDepartment(
                        @Param("department_id") UUID department_id);

        @Query(value = "SELECT * FROM department_of_user " + " where id = :id ", nativeQuery = true)
        DepartmentUser findUserById(@Param("id") UUID id);

        @Query(value = "SELECT * FROM department_of_user"
                        + " where  department_id=:department_id ", nativeQuery = true)

        Page<DepartmentUser> findAllUserOfDepartment(@Param("department_id") UUID department_id, Pageable pageable);

}