package vn.anpha.storage.File.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataDTO;
import vn.anpha.storage.File.Entity.FileDetail;

public interface FileDetailRepository
                extends CrudRepository<FileDetail, UUID>, PagingAndSortingRepository<FileDetail, UUID> {

        @Modifying
        @Query(value = """
                        INSERT INTO file_details (file_id, size, link, is_uploaded,is_uploading, bucket_name, is_version, created_at, updated_at)
                        VALUES (:#{#fileDetail.fileId}, :#{#fileDetail.size}, :#{#fileDetail.link}, :#{#fileDetail.isUploaded}, :#{#fileDetail.isUploading},
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
        void updateUploadStatus(@Param("detailId") UUID detailId, @Param("isUploaded") Boolean isUploaded,
                        @Param("isUploading") Boolean isUploading);

        @Modifying
        @Query(value = """
                        UPDATE file_details
                        SET
                                size = :fileSize
                        WHERE file_id = :detailId
                        """, nativeQuery = true)
        void updateFileSize(@Param("detailId") UUID detailId, @Param("fileSize") Integer fileSize);

        @Query(name = "FileDetail.FindFileMetaDataById", nativeQuery = true)
        Optional<FileMetaDataDTO> findFileMetaDataById(@Param("fileId") UUID fileID);

}
