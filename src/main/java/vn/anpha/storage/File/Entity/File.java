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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.FileDetail.Entity.FileDetail;
import vn.anpha.storage.File_Tag.Entity.FileTag;
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

    // common detail
    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String password;

    // flags
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean hasPassword;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isPrivate;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isPersional;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isInDirectory;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isDirectory;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    // relation department
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id", nullable = false)
    @JsonBackReference
    private Department department;

    // relation for user create
    @ManyToOne
    @JoinColumn(name = "create_user_id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User createBy;

    // relation for file parent
    @ManyToOne
    @JoinColumn(name = "parent_file_id", nullable = false, referencedColumnName = "file_id")
    @JsonBackReference
    private File parentFile;

    @OneToMany(mappedBy = "parentFile", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<File> containFiles;

    // time
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    // soft delete
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    // logs
    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private List<LogUser> logs;

    // version
    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private List<Version> versions;

    // tags
    @OneToMany(mappedBy = "file", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FileTag> tags;

    // filedetail
    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    @JsonManagedReference
    private FileDetail fileDetail;

}
