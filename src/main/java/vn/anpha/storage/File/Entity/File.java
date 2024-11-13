package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.File_Tag.Entity.FileTag;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.History.Entity.LogUser;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.Entity.Version;

@Data
@Entity
@Table(name = "files")
public class File {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "file_id")
    private UUID fileId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean hasPassword;

    @Column(length = 250, nullable = false)
    private String link;

    @Column()
    private Boolean isPersional;

    @Column()
    private Boolean isInFolder;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @JsonBackReference
    private Department department;

    @ManyToOne
    @JoinColumn(name = "create_user_id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User createBy;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false, referencedColumnName = "folder_id")
    @JsonBackReference
    private Folder folder;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Integer fileSize;

    @Column(nullable = true)
    private String path;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isUploaded;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private List<LogUser> logs;

    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private List<Version> versions;

    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FileTag> tagOfFiles;

}
