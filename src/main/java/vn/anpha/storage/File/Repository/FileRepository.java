package vn.anpha.storage.File.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.DTO.Projection.FileProjection;
import vn.anpha.storage.File.Entity.File;

@Repository
public interface FileRepository extends CrudRepository<File, UUID>, PagingAndSortingRepository<File, UUID> {

        @Modifying
        @Query(value = """
                            INSERT INTO files (file_id, name, description, password, has_password, is_in_directory,
                                               is_directory, is_deleted, department_id, create_user_id, parent_file_id,
                                               created_at, updated_at)
                            VALUES (:#{#file.fileId}, :#{#file.name}, :#{#file.description}, :#{#file.password},
                                    :#{#file.hasPassword}, :#{#file.isInDirectory}, :#{#file.isDirectory},
                                    false, :#{#file.department.departmentId}, :#{#file.createBy.userId},
                                    :#{#file.parentFile.fileId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                        """, nativeQuery = true)
        int insertFile(@Param("file") File file);

        @Query(value = "SELECT * FROM files WHERE folder_id=:folder_id AND is_deleted=:is_deleted", nativeQuery = true)
        public List<File> findAllFileInFolder(
                        @Param("folder_id") UUID folderId,
                        @Param("is_deleted") Boolean isDeleted);

        @Query(value = """
                            SELECT
                                f.file_id AS fileId,
                                f.name AS name,
                                f.description AS description,
                                f.has_password AS hasPassword,
                                f.is_in_directory AS isInDirectory,
                                f.is_directory AS isDirectory,
                                f.is_deleted AS isDeleted,
                                f.created_at AS createdAt,
                                f.updated_at AS updatedAt,
                                f.deleted_at AS deletedAt,
                                f.parent_file_id AS parentFileId,
                                f.department_id AS departmentId,
                                fileDetail.size AS size,
                                fileDetail.is_uploaded AS isUploaded,
                                u.email AS email
                            FROM files f
                            LEFT JOIN file_details fileDetail ON f.file_id = fileDetail.file_id
                            LEFT JOIN users u ON f.create_user_id = u.user_id
                            WHERE f.is_deleted = :isDeleted
                            AND f.file_id=:fileId
                            LIMIT 1
                        """, nativeQuery = true)
        public Optional<FileProjection> findFileDetailById(@Param("fileId") UUID fileId,
                        @Param("isDeleted") boolean isDeleted);

        @Query(value = """
                            SELECT
                                f.file_id AS fileId,
                                f.name AS name,
                                f.description AS description,
                                f.has_password AS hasPassword,
                                f.is_in_directory AS isInDirectory,
                                f.is_directory AS isDirectory,
                                f.is_deleted AS isDeleted,
                                f.parent_file_id AS parentFileId,
                                f.department_id as departmentId,
                                f.created_at AS createdAt,
                                f.updated_at AS updatedAt,
                                f.deleted_at AS deletedAt
                            FROM files f
                            WHERE f.is_deleted = :isDeleted
                            AND f.file_id = :fileId
                        """, nativeQuery = true)
        public Optional<FileExistProjection> findFileById(@Param("fileId") UUID fileId,
                        @Param("isDeleted") boolean isDeleted);

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
                        UPDATE files
                        SET
                            is_deleted = :isDeleted,
                            deleted_at = :deletedAt
                        WHERE file_id IN
                            (SELECT file_id from file_system)
                        """, nativeQuery = true)
        void updateDeleteChildFile(@Param("fileId") UUID fileId, @Param("isDeleted") Boolean isDeleted,
                        @Param("deletedAt") LocalDateTime deletedAt);

        @Query(value = """
                            UPDATE files
                            SET
                                is_deleted = :isDeleted,
                                deleted_at = :deletedAt
                            WHERE file_id = :fileId
                        """, nativeQuery = true)
        void updateDeleteFile(@Param("fileId") UUID fileId, @Param("isDeleted") Boolean isDeleted,
                        @Param("deletedAt") LocalDateTime deletedAt);

        @Query(value = """
                        UPDATE files
                        SET parent_file_id = :newParentFileId
                        WHERE file_id = :fileId
                        """, nativeQuery = true)
        void moveFile(@Param("fileId") UUID fileId, @Param("newParentFileId") UUID newParentFileId);

}
