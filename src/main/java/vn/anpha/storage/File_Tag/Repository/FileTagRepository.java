package vn.anpha.storage.File_Tag.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File_Tag.DTO.Request.FileTagCreationRequest;
import vn.anpha.storage.File_Tag.Entity.FileTag;

@Repository
public interface FileTagRepository extends JpaRepository<FileTag, String> {
    @Modifying
    @Query(value = """
            INSERT INTO file_tags (file_id, tag_id, created_at, updated_at)
            VALUES (:#{#fileTag.fileId}, :#{#fileTag.tagId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertFileTag(@Param("fileTag") FileTagCreationRequest fileTag);

}
