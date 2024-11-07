package vn.anpha.storage.Company.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.anpha.storage.Company.Entity.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

        @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM companys WHERE name = :name", nativeQuery = true)
        boolean existsCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        +
                        "FROM companys WHERE name = :name LIMIT 1", nativeQuery = true)
        Company findCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        +
                        "FROM companys WHERE create_by = :createBy", nativeQuery = true)
        List<Company> findAllByCreateBy(@Param("createBy") String createBy);

}
