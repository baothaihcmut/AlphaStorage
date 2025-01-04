package vn.anpha.storage.File.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileContentDTO;
import vn.anpha.storage.File.Entity.FileDetail;

public interface FileDetailRepository
                extends CrudRepository<FileDetail, String>, PagingAndSortingRepository<FileDetail, String> {

        @Modifying
        @Query(value = """
                        INSERT INTO file_details (file_id, size, mime_type, link, is_uploaded,is_uploading, bucket_name, is_version, created_at, updated_at)
                        VALUES (:#{#fileDetail.fileId}, :#{#fileDetail.size}, :#{#fileDetail.mimeType}, :#{#fileDetail.link}, :#{#fileDetail.isUploaded}, :#{#fileDetail.isUploading},
                                :#{#fileDetail.bucketName}, :#{#fileDetail.isVersion}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)""", nativeQuery = true)
        void insertFileDetail(
                        @Param("fileDetail") FileDetailCreationDTO fileDetail);

        @Modifying
        @Query(value = """
                        UPDATE file_details
                        SET
                                is_uploaded = :isUploaded,
                                is_uploading = :isUploading
                        WHERE file_id = :detailId
                        """, nativeQuery = true)
        void updateUploadStatus(@Param("detailId") String detailId, @Param("isUploaded") Boolean isUploaded,
                        @Param("isUploading") Boolean isUploading);

        @Modifying
        @Query(value = """
                        UPDATE file_details
                        SET
                                size = COALESCE(:#{#fileDetail.size}, size),
                                mime_type = COALESCE(:#{#fileDetail.mimeType}, mime_type),
                                is_uploaded = COALESCE(:#{#fileDetail.isUploaded}, is_uploaded),
                                is_uploading = COALESCE(:#{#fileDetail.isUploading}, is_uploading)
                        WHERE file_id = :detailId
                        """, nativeQuery = true)
        void updateFileMetaData(@Param("detailId") String detailId,
                        @Param("fileDetail") UpdateFileContentDTO fileDetail);

        @Query(value = """
                        SELECT
                            fd.file_id as fileId,
                            fd.size as size,
                            fd.link as link,
                            fd.is_uploaded as isUploaded,
                            fd.is_uploading as isUploading,
                            fd.is_version as isVersion,
                            fd.bucket_name as bucketName
                        FROM file_details fd
                        WHERE fd.file_id=:fileId
                                """, nativeQuery = true)
        Optional<FileMetaDataDTO> findFileMetaDataById(@Param("fileId") String fileID);

}
