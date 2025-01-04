package vn.anpha.storage.Department.Repository;

import java.util.List;
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
                                        parent_department_id,
                                        total_size

                                        )
                        VALUES (
                                :departmentId,
                                :#{#department.name},
                                :#{#department.description},
                                :#{#department.companyId},
                                :#{#department.parentDepartmentId},
                               0
                        )
                        """, nativeQuery = true)
        void insertDepartment(@Param("department") DepartmentCreationDTO department, String departmentId);

        @Modifying
        @Query(value = """
                        UPDATE departments
                        SET
                                name =  COALESCE(:#{#department.name}, name),
                                description = COALESCE(:#{#department.description}, description)
                        WHERE department_id = :id
                        """, nativeQuery = true)
        void updateDepartment(@Param("id") String departmentId, @Param("department") DepartmentUpdateDTO department);

        // trả về department theo id
        @Query(value = """
                        SELECT
                                d.department_id as departmentId,
                                d.name as name,
                                d.description as description,
                                d.parent_department_id as parentDepartmentId,
                                d.total_size as totalSize,
                                d.company_id as companyId

                        FROM departments d
                        WHERE d.department_id = :id
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<DepartmentDTO> findDepartmentById(@Param("id") String id);

        // trả về tất cả phòng ban người dùng có thể thao tác
        @Query(value = """
                        SELECT *
                        FROM departments d
                        WHERE  d.company_id = :companyId AND d.department_id IN (
                            SELECT ud.department_id
                            FROM department_of_user ud
                            WHERE ud.user_id =:user_id
                        ) """, nativeQuery = true)
        List<DepartmentDTO> findDepartmentOfUser(@Param("user_id") String user_id,
                        @Param("companyId") String companyId);

        // trả về department Of company_id
        @Query(value = "SELECT d.department_id, d.name, d.description,d.parent_department_id,d.total_size,d.company_id "
                        +
                        "FROM departments d WHERE d.company_id = :companyId", countQuery = "SELECT COUNT(d.department_id) FROM departments d WHERE d.company_id = :companyId", nativeQuery = true)
        Page<DepartmentDTO> FindDepartmentOfCompany(@Param("companyId") String companyId, Pageable pageable);

        @Query(value = """
                        WITH RECURSIVE department_hierarchy AS (
                            -- Initial query: Select the root department
                            SELECT d.department_id, d.name, d.description,d.parent_department_id,d.total_size,d.company_id
                            FROM departments as d
                            WHERE department_id = :departmentId

                            UNION ALL

                            -- Recursive query: Select sub-departments
                            SELECT d.department_id, d.name, d.description,d.parent_department_id,d.total_size,d.company_id
                            FROM departments d
                            INNER JOIN department_hierarchy dh ON d.parent_department_id = dh.department_id
                        )
                        SELECT d.department_id, d.name, d.description,d.parent_department_id,d.total_size,d.company_id
                        FROM department_hierarchy as d
                        """, nativeQuery = true)
        List<Department> findAllSubDepartments(@Param("departmentId") String departmentId);

}
