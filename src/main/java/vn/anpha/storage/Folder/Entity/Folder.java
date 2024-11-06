package vn.anpha.storage.Folder.Entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;
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
import lombok.ToString;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.User.Entity.User;

@ToString
@Data
@Entity
@Table(name = "folders")
public class Folder {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "folder_id")
    private UUID folderId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private BigInteger total_size;

    @Column(nullable = false)
    private BigInteger limit_size;

    @Column(name = "is_personal")
    private Boolean isPersonal;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<File> containFiles;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", referencedColumnName = "folder_id")
    @JsonBackReference
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<Folder> subFolders;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @JsonBackReference
    private Department department;

    @ManyToOne
    @JoinColumn(name = "persional_user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User persionalUserId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}