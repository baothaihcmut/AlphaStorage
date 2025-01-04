package vn.anpha.storage.File_Tag.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File_Tag.DTO.Projection.FileTagDTO;
import vn.anpha.storage.File_Tag.DTO.Request.FileTagCreationRequest;
import vn.anpha.storage.File_Tag.Entity.FileTag;
import vn.anpha.storage.File_Tag.Entity.FileTagId;

@Repository
public interface FileTagRepository extends JpaRepository<FileTag, FileTagId> {
    @Modifying
    @Query(value = """
            INSERT INTO file_tags (file_id, tag_id, created_at, updated_at)
            VALUES (:#{#fileTag.fileId}, :#{#fileTag.tagId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertFileTag(@Param("fileTag") FileTagCreationRequest fileTag);

    @Modifying
    @Query(value = """
            DELETE FROM file_tags
            WHERE file_id = :fileId
            AND tag_id = :tagId
            """, nativeQuery = true)
    void deleteFileTag(@Param("fileId") String fileId, @Param("tagId") String tagId);

    @Query(value = """
            SELECT
                ft.file_id as fileId,
                ft.tag_id as tagId,
                t.name as tagName,
                t.is_company_tag as isCompanyTag
            FROM file_tags ft
            JOIN tags t ON ft.tag_id = t.tag_id
            WHERE ft.file_id = :fileId
            """, nativeQuery = true)
    List<FileTagDTO> findFileTagByFileId(@Param("fileId") String fileId);

}
