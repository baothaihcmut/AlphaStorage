package vn.anpha.storage.Department.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentCreationDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateDTO;
import vn.anpha.storage.Department.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {

        @Modifying
        @Query(value = """
                        INSERT INTO
                                departments (
                                        department_id,
                                        name,
                                        description,
                                        company_id,
                                        created_at,
                                        updated_at)
                        VALUES (
                                :#{#department.departmentId},
                                :#{#department.name},
                                :#{#department.description},
                                :#{#department.companyId},
                                CURRENT_TIMESTAMP,
                                CURRENT_TIMESTAMP
                        )
                        """, nativeQuery = true)
        void insertDepartment(@Param("department") DepartmentCreationDTO department);

        @Modifying
        @Query(value = """
                        UPDATE departments
                        SET
                                name =  COALESCE(:#{#department.name}, name),
                                description = COALESCE(:#{#department.description}, description),
                                updated_at = CURRENT_TIMESTAMP
                        WHERE department_id = :id
                        """, nativeQuery = true)
        void updateDepartment(@Param("id") String departmentId, @Param("department") DepartmentUpdateDTO department);

        // trả về department theo id
        @Query(value = """
                        SELECT
                                d.department_id as departmentId,
                                d.name as name,
                                d.description as description,
                                d.total_size as totalSize
                        FROM departments d
                        WHERE d.department_id = :id
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<DepartmentDTO> findDepartmentById(@Param("id") String id);

        @Query(value = """
                        SELECT
                                d.department_id as departmentId,
                                d.name as name,
                                d.description as description,
                                d.total_size as totalSize,
                                d.company_id as companyId
                        FROM departments d
                        JOIN companies c ON d.company_id = c.company_id
                        JOIN users u ON c.owner_id = u.user_id
                        WHERE d.department_id = :id AND u.email = :owner_email
                        """, nativeQuery = true)
        Optional<DepartmentDTO> findDepartmentByIdAndCheckOwn(@Param("id") String id,
                        @Param("owner_email") String ownerEmail);

        // trả về department Of company_id
        @Query(value = "SELECT d.department_id, d.name, d.description " +
                        "FROM departments d WHERE d.company_id = :companyId", countQuery = "SELECT COUNT(d.department_id) FROM departments d WHERE d.company_id = :companyId", nativeQuery = true)
        Page<DepartmentDTO> FindDepartmentOfCompany(@Param("companyId") String companyId,

                        Pageable pageable);

}
