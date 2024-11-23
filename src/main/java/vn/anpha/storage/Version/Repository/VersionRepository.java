package vn.anpha.storage.Version.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Version.DTO.request.VersionCreationDTO;
import vn.anpha.storage.Version.DTO.response.VersionDTO;
import vn.anpha.storage.Version.Entity.Version;

public interface VersionRepository extends JpaRepository<Version, UUID> {
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
    void deleteVersion(@Param("versionId") UUID versionId);

    @Query(name = "Version.FindVersionById", nativeQuery = true)
    Optional<VersionDTO> findVersionById(@Param("versionId") UUID versionId);

    @Query(name = "Version.FindVersionDetailById", nativeQuery = true)
    Optional<VersionDTO> findVersionDetailById(@Param("versionId") UUID versionId);

    @Query(name = "Version.FindAllVersionOfFile", nativeQuery = true)
    List<VersionDTO> findAllVersionOfFile(@Param("fileId") UUID fileId);
}
