package vn.anpha.storage.File.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
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
    void updateFile(@Param("fileId") UUID fileId, @Param("fileUpdateInfo") FileUpdateInfoDTO fileUpdateInfo);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                parent_file_id = :#{#moveFileDTO.newDirectoryId},
                updated_at = CURRENT_TIMESTAMP
            WHERE file_id = :fileId
            """, nativeQuery = true)
    void moveFile(@Param("fileId") UUID fileID, @Param("moveFileDTO") MoveFileDTO moveFileDTO);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                is_deleted = true,
                deleted_at = CURRENT_TIMESTAMP
            WHERE file_id=:fileId
            """, nativeQuery = true)
    void softDeleteFile(@Param("fileId") UUID fileId);

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
    void softDeleteChild(@Param("fileId") UUID fileId);

    @Modifying
    @Query(value = """
            UPDATE files
            SET
                is_deleted = false,
                parent_file_id = :#{#recoverFileDTO.recoverDirectoryId}
                updated_at = CURRENT_TIMESTAMP
            WHERE file_id=:fileId
            """, nativeQuery = true)
    void recoverFile(@Param("fileId") UUID fileId, @Param("recoverFileDTO") RecoverFileDTO recoverFileDTO);

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
    void recoverChild(@Param("fileId") UUID fileId);

    @Query(name = "File.findAllFileInDirectory", nativeQuery = true)
    public List<FileDTO> findAllFileInDirectory(
            @Param("isDeleted") boolean isDeleted,
            @Param("parentFileId") UUID parentId);

    @Query(name = "File.findFileDetailById", nativeQuery = true)
    public Optional<FileDetailDTO> findFileDetailById(@Param("fileId") UUID fileId,
            @Param("isDeleted") boolean isDeleted);

    @Query(name = "File.findFileById", nativeQuery = true)
    public Optional<FileDTO> findFileById(@Param("fileId") UUID fileId,
            @Param("isDeleted") boolean isDeleted);

}
