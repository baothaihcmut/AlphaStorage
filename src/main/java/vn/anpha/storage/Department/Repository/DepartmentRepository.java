package vn.anpha.storage.Department.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Department.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

        @Query(value = "SELECT * FROM departments WHERE department_id=:id LIMIT 1", nativeQuery = true)
        Optional<Department> findDepartmentById(@Param("id") UUID id);

}
