package vn.anpha.storage.Company.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Company.Entity.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        +
                        "FROM companys WHERE name = :name LIMIT 1", nativeQuery = true)
        Company findCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        +
                        "FROM companys WHERE create_by = :createBy", nativeQuery = true)
        List<Company> findAllByCreateBy(@Param("createBy") String createBy);

        @Query(value = "SELECT count(*) FROM companys WHERE name = :name", nativeQuery = true)
        int existsCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id,name, total_size, limit_size FROM companys WHERE id=:company_id LIMIT 1", nativeQuery = true)
        Optional<Company> findCompanyNameAndSize(@Param("company_id") UUID companyId);
}
