package vn.anpha.storage.Tag.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Tag.DTO.Projection.TagCompanyDTO;
import vn.anpha.storage.Tag.DTO.Request.TagCreationRequest.TagCompanyCreationRequest;
import vn.anpha.storage.Tag.Entity.TagCompany;

@Repository
public interface TagCompanyRepository extends JpaRepository<TagCompany, String> {
    @Modifying
    @Query(value = """
            INSERT INTO tag_companies (tag_id, company_id, created_at, updated_at)
            VALUES (:#{#tagCompany.tagId}, :#{#tagCompany.companyId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertTagCompany(@Param("tagCompany") TagCompanyCreationRequest tagCompany);

    @Query(value = """
            SELECT
                t.tag_id as tagId,
                t.name as name,
                t.is_company_tag as isCompanyTag,
                tc.company_id as companyId
            FROM tag_companies tc
            JOIN tags t ON tc.tag_id = t.tag_id
            WHERE tc.tag_id = :tagId
            LIMIT 1
            """, nativeQuery = true)
    Optional<TagCompanyDTO> findTagCompanyById(@Param("tagId") String tagId);

    @Query(value = """
            SELECT
                t.tag_id as tagId,
                t.name as name,
                t.is_company_tag as isCompanyTag,
                tc.company_id as companyId
            FROM tag_companies tc
            JOIN tags t ON tc.tag_id = t.tag_id
            WHERE tc.company_id = :companyId""", nativeQuery = true)
    List<TagCompanyDTO> findTagCompanyByCompanyId(@Param("companyId") String companyId);
}
