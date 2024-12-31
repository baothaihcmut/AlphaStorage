package vn.anpha.storage.Version.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.User.Entity.User;

@ToString
@Getter
@Setter
@Entity
@Table(name = "versions")
public class Version {
    @Id
    @Column(name = "version_id")
    private String versionId;

    @Column(nullable = false, columnDefinition = "Text")
    private String link;

    @Column(nullable = true, columnDefinition = "Text")
    private String description;

    @Column(nullable = false)
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false, referencedColumnName = "file_id")
    @JsonBackReference
    private File file;

    @ManyToOne
    @JoinColumn(name = "update_user_id", nullable = false)
    @JsonBackReference
    private User updateUser;

    @Column(updatable = false)
    private LocalDateTime createdAt;

}
