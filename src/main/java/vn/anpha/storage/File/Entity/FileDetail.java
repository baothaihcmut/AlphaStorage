package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "file_details")
public class FileDetail {
    @Id
    @Column(name = "file_id")
    private String fileId;

    // file detail
    @Column(nullable = false)
    private Integer size;

    @Column(length = 250, nullable = false)
    private String link;

    @Column(nullable = false)
    private String mimeType;

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
