package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.File.DTO.Response.FileMetaDataDTO;

@Entity
@Data
@Table(name = "file_details")
@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "FileDetail.FindFileMetaDataById", query = """
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
                """, resultSetMapping = "MetaDataDTOMapping")
})
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(name = "MetaDataDTOMapping", classes = @ConstructorResult(targetClass = FileMetaDataDTO.class, columns = {
                @ColumnResult(name = "fileId", type = byte[].class),
                @ColumnResult(name = "size", type = Integer.class),
                @ColumnResult(name = "link", type = String.class),
                @ColumnResult(name = "isUploaded", type = Boolean.class),
                @ColumnResult(name = "isUploading", type = Boolean.class),
                @ColumnResult(name = "isVersion", type = Boolean.class),
                @ColumnResult(name = "bucketName", type = String.class)
        }))
})
public class FileDetail {
    @Id
    @Column(name = "file_id", columnDefinition = "VARCHAR(36)")
    private String fileId;

    // file detail
    @Column(nullable = false)
    private Integer size;

    @Column(length = 250, nullable = false)
    private String link;

    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private Boolean isUploaded;

    @Column(nullable = false)
    private Boolean isUploading;

    // relation with file
    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "file_id")
    @JsonBackReference
    private File file;

    // time
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "BOOLEAN DEFAULT false", nullable = false)
    private Boolean isVersion;

    @Column(nullable = false)
    private String bucketName;

}
