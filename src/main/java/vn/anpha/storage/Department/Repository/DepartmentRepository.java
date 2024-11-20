package vn.anpha.storage.Department.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Department.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

        @Query(value = "SELECT department_id,name FROM departments WHERE department_id=:id LIMIT 1", nativeQuery = true)
        Optional<Department> findDepartmentById(@Param("id") UUID id);

        // trả về department theo id
        @Query(value = "SELECT d.department_id  as departmentId, d.name " +
                        "FROM departments d " +
                        "WHERE d.department_id = :id" +
                        "LIMIT 1", nativeQuery = true)
        DepartmenResponseProjection findDepartmentwithId(@Param("id") UUID id);

        @Query(value = "SELECT d.department_id as departmentId, d.name  " +
                        "FROM departments d " +
                        "JOIN companys c ON d.company_id = c.company_id " +
                        "WHERE d.department_id = :id AND c.create_by = :create_by " +
                        "LIMIT 1", nativeQuery = true)
        DepartmenResponseProjection findDepartmentByIdAndCheckOwn(@Param("id") UUID id,
                        @Param("create_by") String emailLogin);

        // trả về department Of company_id
        @Query(value = "SELECT d.department_id, d.name, d.description " +
                        "FROM departments d WHERE d.company_id = :companyId", countQuery = "SELECT COUNT(d.department_id) FROM departments d WHERE d.company_id = :companyId", nativeQuery = true)
        Page<DepartmenResponseProjection> FindDepartmentOfCompany(@Param("companyId") UUID companyId,
                        Pageable pageable);

}
