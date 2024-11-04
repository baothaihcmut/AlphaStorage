package vn.anpha.storage.Folder.Entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import vn.anpha.storage.User_Folder.Entity.FolderOfUser;
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
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.File.Entity.File;

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

    private BigInteger total_size;
    private BigInteger limit_size;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<File> containFiles;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", referencedColumnName = "folder_id")
    @JsonBackReference
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<Folder> subFolders;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<FolderOfUser> managers;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}