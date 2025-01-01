package vn.anpha.storage.Version.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Version.DTO.projection.VersionDTO;
import vn.anpha.storage.Version.DTO.projection.VersionDetailDTO;
import vn.anpha.storage.Version.DTO.request.VersionCreationDTO;
import vn.anpha.storage.Version.Entity.Version;

public interface VersionRepository extends JpaRepository<Version, String> {
        @Modifying
        @Query(value = """
                        INSERT INTO versions(version_id,link,description,size,file_id,update_user_id,created_at)
                        VALUES
                            (:#{#versionDTO.versionId}, :#{#versionDTO.link},
                             :#{#versionDTO.description}, :#{#versionDTO.size},
                             :#{#versionDTO.fileId}, :#{#versionDTO.updateUserId},
                             :#{#versionDTO.createdAt}
                            )
                        """, nativeQuery = true)
        void insertVersion(@Param("versionDTO") VersionCreationDTO versionDTO);

        @Modifying
        @Query(value = """
                        DELETE FROM versions WHERE version_id=:versionId
                        """, nativeQuery = true)
        void deleteVersion(@Param("versionId") String versionId);

        @Query(value = """
                        SELECT
                                v.version_id AS versionId,
                                v.link AS link,
                                v.description AS description,
                                v.size AS size,
                                v.file_id AS fileId,
                                v.update_user_id AS updateUserId,
                                v.created_at AS createdAt
                            FROM versions v
                            WHERE v.version_id=:versionId
                            LIMIT 1
                        """, nativeQuery = true)
        Optional<VersionDTO> findVersionById(@Param("versionId") UUID versionId);

        @Query(value = """
                        SELECT
                                v.version_id AS versionId,
                                v.link as link,
                                v.description AS description,
                                v.size AS size,
                                v.created_at createdAt,
                                f.file_id AS fileId,
                                f.name AS fileName,
                                u.user_id AS updateUserId,
                                u.email AS updateUserEmail
                            FROM versions v
                            LEFT JOIN files f ON v.file_id = f.file_id
                            LEFT JOIN users u ON v.update_user_id = u.user_id
                            WHERE v.version_id=:versionId
                            LIMIT 1
                        """, nativeQuery = true)
        Optional<VersionDetailDTO> findVersionDetailById(@Param("versionId") UUID versionId);

        @Query(value = """
                        SELECT
                                v.version_id AS versionId,
                                v.link AS link,
                                v.description AS description,
                                v.size AS size,
                                v.file_id AS fileId,
                                v.update_user_id AS updateUserId,
                                v.created_at AS createdAt
                            FROM versions v
                            WHERE v.file_id=:fileId
                        """, nativeQuery = true)
        List<VersionDTO> findAllVersionOfFile(@Param("fileId") String fileId);
}
