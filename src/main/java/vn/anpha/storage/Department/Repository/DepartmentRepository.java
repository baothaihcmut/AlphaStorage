package vn.anpha.storage.Department.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Department.Entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

}
