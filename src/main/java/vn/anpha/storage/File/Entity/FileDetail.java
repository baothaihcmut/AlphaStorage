package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "file_details")
public class FileDetail {
    @Id
    private UUID id;

    // file detail
    @Column(nullable = false)
    private Integer size;

    @Column(length = 250, nullable = false)
    private String link;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isUploaded;

    // relation with file
    @OneToOne(optional = false)
    @MapsId
    @JsonBackReference
    private File file;

    // time
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVersion;

}
