package vn.anpha.storage.Company.Repository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Company.DTO.projections.CompanySizeProjection;
import vn.anpha.storage.Company.Entity.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        +
                        "FROM companies WHERE name = :name LIMIT 1", nativeQuery = true)
        Company findCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id, name, description, total_size, limit_size, create_by, created_at, updated_at "
                        + "FROM companies WHERE create_by = :createBy", nativeQuery = true)
        Page<Company> findAllByCreateBy(@Param("createBy") String createBy, Pageable pageable);

        @Query(value = "SELECT count(*) FROM companies WHERE name = :name", nativeQuery = true)
        int existsCompanyByName(@Param("name") String name);

        @Query(value = "SELECT company_id as companyId,name as name, total_size as totalSize, limit_size as limitSize, has_version as hasVersion FROM companies WHERE company_id=:company_id LIMIT 1", nativeQuery = true)
        Optional<CompanySizeProjection> findCompanyNameAndSize(@Param("company_id") UUID companyId);

        @Query(value = "SELECT * "
                        + "FROM companies WHERE company_id = :companyId and create_by=:create_by", nativeQuery = true)
        Company findCompanyByIdAndOwn(@Param("companyId") UUID companyId,
                        @Param("create_by") String create_by);

        @Modifying
        @Query(value = """
                        UPDATE companies
                        SET total_size = :newSize
                        WHERE company_id = :companyId
                        """, nativeQuery = true)
        void updateCompanySize(@Param("companyId") UUID companyId, @Param("newSize") BigInteger newSize);
}
