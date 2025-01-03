package vn.anpha.storage.File_Tag.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.Tag.Entity.Tag;

@Entity
@Data
@Table(name = "file_tags")
@IdClass(FileTagId.class)
public class FileTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false, referencedColumnName = "file_id")
    @JsonBackReference
    private File file;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false, referencedColumnName = "tag_id")
    @JsonBackReference
    private Tag tag;
    @Column(updatable = false)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
