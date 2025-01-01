package vn.anpha.storage.File.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.FileDetailDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.Entity.File;

@Repository
public interface FileRepository extends CrudRepository<File, String>, PagingAndSortingRepository<File, String> {

    @Modifying
    @Query(value = """
                INSERT INTO files (file_id, name, description, password, has_password, is_in_directory,
                                   is_directory, is_deleted, department_id, create_user_id, parent_file_id,
                                   created_at, updated_at)
                VALUES (:#{#file.fileId}, :#{#file.name}, :#{#file.description}, :#{#file.password},
                        :#{#file.hasPassword}, :#{#file.isInDirectory}, :#{#file.isDirectory},
                        false, :#{#file.departmentId}, :#{#file.createUserId},
                        :#{#file.parentFileId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertFile(@Param("file") FileCreationDTO fileCreationDTO);

    @Modifying
    @Query(value = """
                UPDATE files
                SET name = COALESCE(:#{#fileUpdateInfo.name}, name),
                    description = COALESCE(:#{#fileUpdateInfo.description}, description),
                    password = COALESCE(:#{#fileUpdateInfo.password}, password),
                    has_password = COALESCE(:#{#fileUpdateInfo.hasPassword}, has_password),
                    updated_at = CURRENT_TIMESTAMP
                WHERE file_id = :fileId
            """, nativeQuery = true)
    void updateFile(@Param("fileId") String fileId, @Param("fileUpdateInfo") FileUpdateInfoDTO fileUpdateInfo);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                parent_file_id = :#{#moveFileDTO.newDirectoryId},
                updated_at = CURRENT_TIMESTAMP
            WHERE file_id = :fileId
            """, nativeQuery = true)
    void moveFile(@Param("fileId") String fileID, @Param("moveFileDTO") MoveFileDTO moveFileDTO);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                is_deleted = true,
                deleted_at = CURRENT_TIMESTAMP
            WHERE file_id=:fileId
            """, nativeQuery = true)
    void softDeleteFile(@Param("fileId") String fileId);

    @Modifying
    @Query(value = """
            WITH RECURSIVE file_system(file_id) AS (
                SELECT f.file_id
                FROM files f
                WHERE f.parent_file_id = :fileId
                UNION ALL
                SELECT f.file_id
                FROM files f
                INNER JOIN file_system fs
                ON f.parent_file_id = fs.file_id
            )
            UPDATE files
            SET
                is_deleted = true,
                deleted_at = CURRENT_TIMESTAMP
            WHERE file_id IN
                (SELECT file_id from file_system)
            """, nativeQuery = true)
    void softDeleteChild(@Param("fileId") String fileId);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                is_deleted = false,
                parent_file_id = :#{#recoverFileDTO.recoverDirectoryId},
                updated_at = CURRENT_TIMESTAMP,
                deleted_at = NULL
            WHERE file_id=:fileId
            """, nativeQuery = true)
    void recoverFile(@Param("fileId") String fileId, @Param("recoverFileDTO") RecoverFileDTO recoverFileDTO);

    @Modifying
    @Query(value = """
            WITH RECURSIVE file_system(file_id) AS (
                SELECT f.file_id
                FROM files f
                WHERE f.parent_file_id = :fileId
                UNION ALL
                SELECT f.file_id
                FROM files f
                INNER JOIN file_system fs
                ON f.parent_file_id = fs.file_id
            )
            UPDATE files
            SET
                is_deleted = false,
                updated_at = CURRENT_TIMESTAMP
            WHERE file_id IN
                (SELECT file_id from file_system)
            """, nativeQuery = true)
    void recoverChild(@Param("fileId") String fileId);

    @Query(value = """
            SELECT
                    file_id AS fileId,
                    name,
                    description,
                    has_password AS hasPassword,
                    is_in_directory AS isInDirectory,
                    is_directory AS isDirectory,
                    is_deleted AS isDeleted,
                    department_id AS departmentId,
                    create_user_id AS createUserId,
                    parent_file_id AS parentFileId,
                    created_at AS createdAt,
                    updated_at AS updatedAt,
                    deleted_at AS deletedAt
                FROM files
                WHERE is_deleted=:isDeleted
                AND parent_file_id=:parentFileId
            """, nativeQuery = true)
    public List<FileDTO> findAllFileInDirectory(
            @Param("isDeleted") boolean isDeleted,
            @Param("parentFileId") String parentId);

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
                    fd.size AS fileDetailSize,
                    fd.is_uploaded AS fileDetailIsUploaded,
                    fd.is_uploading AS fileDetailIsUploading,
                    fd.is_version AS fileDetailIsVersion,
                    u.user_id AS createUserId,
                    u.email AS createUserEmail,
                    d.department_id AS departmentId,
                    d.name departmentName
                FROM files f
                LEFT JOIN file_details fd ON f.file_id = fd.file_id
                LEFT JOIN users u ON f.create_user_id = u.user_id
                LEFT JOIN departments d ON f.department_id = d.department_id
                WHERE f.is_deleted = :isDeleted
                AND f.file_id = :fileId
                LIMIT 1
            """, nativeQuery = true)
    public Optional<FileDetailDTO> findFileDetailById(@Param("fileId") String fileId,
            @Param("isDeleted") boolean isDeleted);

    @Query(value = """
            SELECT
                    file_id AS fileId,
                    name,
                    description,
                    has_password AS hasPassword,
                    is_in_directory AS isInDirectory,
                    is_directory AS isDirectory,
                    is_deleted AS isDeleted,
                    department_id AS departmentId,
                    create_user_id AS createUserId,
                    parent_file_id AS parentFileId,
                    created_at AS createdAt,
                    updated_at AS updatedAt,
                    deleted_at AS deletedAt
                FROM files
                WHERE file_id=:fileId
                AND is_deleted=:isDeleted
                LIMIT 1""", nativeQuery = true)

    public Optional<FileDTO> findFileById(@Param("fileId") String fileId,
            @Param("isDeleted") boolean isDeleted);

}
