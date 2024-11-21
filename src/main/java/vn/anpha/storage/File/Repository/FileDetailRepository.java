package vn.anpha.storage.File.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.File.DTO.Projection.FileDetailProjection;
import vn.anpha.storage.File.Entity.FileDetail;

public interface FileDetailRepository
                extends CrudRepository<FileDetail, UUID>, PagingAndSortingRepository<FileDetail, UUID> {

        @Modifying
        @Query(value = """
                        INSERT INTO file_details (file_id, size, link, is_uploaded, bucket_name, is_version, created_at, updated_at)
                        VALUES (:#{#fileDetail.fileId}, :#{#fileDetail.size}, :#{#fileDetail.link}, :#{#fileDetail.isUploaded},
                                :#{#fileDetail.bucketName}, :#{#fileDetail.isVersion}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)""", nativeQuery = true)
        void insertFileDetail(@Param("fileDetail") FileDetail fileDetail);

        @Query(value = """
                        UPDATE file_details
                        SET is_uploaded = :isUploaded
                        WHERE id = :detailId
                        """, nativeQuery = true)
        void updateUploadStatus(@Param("detailId") UUID detailId, @Param("isUploaded") Boolean isUploaded);

        @Query(value = """
                        SELECT
                        f.id AS id,
                        f.size AS size,
                        f.link AS link,
                        f.isUploaded AS isUploaded,
                        f.bucketName AS bucketName,
                        f.isVersion AS isVersion,
                        f.createdAt AS createdAt,
                        f.updatedAt AS updatedAt
                        FROM file_details f
                        WHERE f.id = :fileId
                                """, nativeQuery = true)
        Optional<FileDetailProjection> findFileDetailById(@Param("fileId") UUID fileId);

        @Query(value = """
                        SELECT
                                f.id AS id,
                                f.size AS size,
                                f.link AS link,
                                f.isUploaded AS isUploaded,
                                f.bucketName AS bucketName,
                                f.isVersion AS isVersion,
                                f.createdAt AS createdAt,
                                f.updatedAt AS updatedAt
                        FROM file_details f
                        WHERE f.id = :fileId
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<FileDetailProjection> findLinkOfFile(@Param("fileId") UUID fileId);

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
                        SELECT
                                f.id AS id,
                                f.size AS size,
                                f.link AS link,
                                f.isUploaded AS isUploaded,
                                f.bucketName AS bucketName,
                                f.isVersion AS isVersion,
                                f.createdAt AS createdAt,
                                f.updatedAt AS updatedAt
                        FROM file_details f
                        WHERE f.id IN (SELECT file_id FROM file_system)
                        """, nativeQuery = true)
        List<FileDetailProjection> findLinkOfAllChild(@Param("fileId") UUID fileId);

}
