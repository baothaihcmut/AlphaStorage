package vn.anpha.storage.File.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.File.Entity.FileDetail;

public interface FileDetailRepository
        extends CrudRepository<FileDetail, UUID>, PagingAndSortingRepository<FileDetail, UUID> {
    @Query(value = """
            UPDATE file_details
            SET is_uploaded = :isUploaded
            WHERE id = :detailId
            """, nativeQuery = true)
    void updateUploadStatus(@Param("detailId") UUID detailId, @Param("isUploaded") Boolean isUploaded);

    @Query(value = """
            SELECT link
            FROM file_details
            WHERE id = :fileId
            LIMIT 1
            """, nativeQuery = true)
    Optional<String> findLinkOfFile(@Param("fileId") UUID fileId);

    @Query(value = """
            WITH RECURSIVE file_system(file_id) AS (
                SELECT f.file_id
                FROM files f
                WHERE f.file_id = :fileId
                UNION ALL
                SELECT f.file_id
                FROM files f
                INNER JOIN file_system fs
                ON f.parent_file_id = fs.file_id
            )
            SELECT link
            FROM file_details
            WHERE id IN (SELECT file_id FROM file_system)
            """, nativeQuery = true)
    List<String> findLinkOfAllChild(@Param("fileId") UUID fileId);

}
