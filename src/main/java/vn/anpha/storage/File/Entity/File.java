package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
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
    private UUID id;

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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Integer fileSize;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "file")
    private List<LogUser> logs;

    @OneToMany(mappedBy = "file")
    private List<Version> versions;

}
