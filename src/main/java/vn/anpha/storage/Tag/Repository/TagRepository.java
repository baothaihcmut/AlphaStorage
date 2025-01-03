package vn.anpha.storage.Tag.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Tag.DTO.Projection.TagDTO;
import vn.anpha.storage.Tag.DTO.Request.TagCreationRequest;
import vn.anpha.storage.Tag.Entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    @Modifying
    @Query(value = """
            INSERT INTO tags (tag_id, name,is_company_tag, created_at, updated_at)
            VALUES (:#{#tag.tagId}, :#{#tag.name},:#{#tag.isCompanyTag}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertTag(@Param("tag") TagCreationRequest tag);

    @Query(value = """
            SELECT
                t.tag_id as tagId,
                t.name as name,
                t.is_company_tag as isCompanyTag
            FROM tags t
            WHERE t.tag_id = :tagId
            LIMIT 1
            """, nativeQuery = true)
    Optional<TagDTO> findTagById(@Param("tagId") String tagId);

    @Query(value = """
            SELECT
            t.tag_id as tagId,
            t.name as name,
            t.is_company_tag as isCompanyTag
            FROM tags t
            """, nativeQuery = true)
    List<TagDTO> findAllTag();

    @Query(value = """
            SELECT
                t.tag_id as tagId,
                t.name as name,
                t.is_company_tag as isCompanyTag
            FROM tags t
            JOIN file_tags ft ON t.tag_id = ft.tag_id
            WHERE ft.file_id = :fileId
            """, nativeQuery = true)
    List<TagDTO> findTagByFileId(@Param("fileId") String fileId);

}
