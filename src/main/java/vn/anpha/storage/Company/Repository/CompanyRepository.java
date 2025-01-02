package vn.anpha.storage.Company.Repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Company.DTO.projections.CompanySizeProjection;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.interfaceCompany.CompanyInterface;
import vn.anpha.storage.User_Department.Repository.UserDepartmentResponseProjection;

public interface CompanyRepository extends JpaRepository<Company, String> {
    @Query(value = """
            SELECT company_id, name, description, total_size, limit_size, owner_id, created_at, updated_at
            FROM companies
            WHERE name = :name LIMIT 1
            """, nativeQuery = true)
    Company findCompanyByName(@Param("name") String name);

    @Query(value = """
            SELECT company_id, name, description, total_size, limit_size,has_version, owner_id, created_at, updated_at
            FROM companies
            WHERE owner_id = :owner_id
            """, nativeQuery = true)
    Page<CompanyInterface> findAllByCreateBy(@Param("owner_id") String owner_id, Pageable pageable);

    @Query(value = """
            SELECT company_id as companyId,name as name, total_size as totalSize, limit_size as limitSize, has_version as hasVersion
            FROM companies
            WHERE company_id=:company_id LIMIT 1
            """, nativeQuery = true)
    Optional<CompanySizeProjection> findCompanyNameAndSize(@Param("company_id") String companyId);

    @Query(value = "SELECT count(employee_id) FROM users_of_company WHERE company_id=:company_id And employee_id=:employee_id  ", nativeQuery = true)
    Long checkUserInCompany(String employee_id, String company_id);

    // lấy thông tin của công ty bằng id +check quyền sở hữu
    @Query(value = """
            SELECT *
            FROM companies
            WHERE company_id = :companyId
            """, nativeQuery = true)
    Optional<CompanyInterface> findCompanyById(@Param("companyId") String companyId);

    // @Query(value = """
    // SELECT *
    // FROM companies
    // WHERE company_id = :companyId
    // """, nativeQuery = true)
    // CompanyInterface findCompanyLimitSizeById(@Param("companyId") String
    // companyId);

    @Modifying
    @Query(value = """
            UPDATE companies
            SET limit_size = :newSize
            WHERE company_id = :companyId and owner_id=:owner_id
            """, nativeQuery = true)
    void updateCompanySize(@Param("companyId") String companyId, @Param("newSize") BigInteger newSize,
            @Param("owner_id") String owner_id);

    @Modifying
    @Query(value = """
                UPDATE companies
                SET name = COALESCE(:name, name),
                    description = COALESCE(:description, description)
                WHERE company_id = :companyId AND owner_id = :ownerId
            """, nativeQuery = true)
    void updateCompanyInfo(@Param("companyId") String companyId,
            @Param("ownerId") String ownerId,
            @Param("name") String name,
            @Param("description") String description);

    // thêm công ty mới
    @Modifying
    @Query(value = """
            INSERT INTO companies (company_id, owner_id, name, total_size, description, limit_size, has_version, created_at, updated_at)
            VALUES (:companyId, :ownerId, :#{#company.name}, 0, :#{#company.description}, :#{#company.limit_size}, :#{#company.hasVersion}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertCompany(@Param("company") CompanyCreationRequest company, @Param("companyId") String companyId,
            @Param("ownerId") String ownerId);

    @Query(value = """
            SELECT COUNT(*)
            FROM companies
            WHERE owner_id = :ownerId AND company_id = :companyId
            """, nativeQuery = true)
    Long CheckOwnCompany(@Param("companyId") String companyId, @Param("ownerId") String ownerId);

}
