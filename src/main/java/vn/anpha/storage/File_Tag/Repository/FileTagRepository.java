package vn.anpha.storage.File_Tag.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.File_Tag.DTO.Request.FileTagCreationRequest;

public interface FileTagRepository {
    @Modifying
    @Query(value = """
            INSERT INTO file_tags (file_id, tag_id, created_at, updated_at)
            VALUES (:#{#fileTag.fileId}, :#{#fileTag.tagId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertFileTag(@Param("fileTag") FileTagCreationRequest fileTag);

}
