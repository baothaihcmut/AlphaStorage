package vn.anpha.storage.File.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.File.DTO.Response.FileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File_Tag.Entity.FileTag;
import vn.anpha.storage.History.Entity.LogUser;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.Entity.Version;

@Data
@Entity
@Table(name = "files")
@NamedNativeQueries(value = {
        @NamedNativeQuery(name = "File.findFileById", query = """
                SELECT
                    file_id AS fileId,
                    name,
                    description,
                    has_password AS hasPassword,
                    is_in_directory AS isInDirectory,
                    is_directory AS isDirectory,
                    is_deleted AS isDeleted,
                    department_id AS departmentId,
                    create_user_id AS createUserId,
                    parent_file_id AS parentFileId,
                    created_at AS createdAt,
                    updated_at AS updatedAt,
                    deleted_at AS deletedAt
                FROM files
                WHERE file_id=:fileId
                AND is_deleted=:isDeleted
                LIMIT 1""", resultSetMapping = "FileDTOMapping"),
        @NamedNativeQuery(name = "File.findFileDetailById", query = """
                SELECT
                    f.file_id AS fileId,
                    f.name AS name,
                    f.description AS description,
                    f.has_password AS hasPassword,
                    f.is_in_directory AS isInDirectory,
                    f.is_directory AS isDirectory,
                    f.is_deleted AS isDeleted,
                    f.created_at AS createdAt,
                    f.updated_at AS updatedAt,
                    f.deleted_at AS deletedAt,
                    f.parent_file_id AS parentFileId,
                    fd.size AS fileDetailSize,
                    fd.is_uploaded AS fileDetailIsUploaded,
                    fd.is_uploading AS fileDetailIsUploading,
                    fd.is_version AS fileDetailIsVersion,
                    u.user_id AS createUserId,
                    u.email AS createUserEmail,
                    d.department_id AS departmentId,
                    d.name departmentName
                FROM files f
                LEFT JOIN file_details fd ON f.file_id = fd.file_id
                LEFT JOIN users u ON f.create_user_id = u.user_id
                LEFT JOIN departments d ON f.department_id = d.department_id
                WHERE f.is_deleted = :isDeleted
                AND f.file_id = :fileId
                LIMIT 1""", resultSetMapping = "FileDetailDTOMapping"),
        @NamedNativeQuery(name = "File.findAllFileInDirectory", query = """
                SELECT
                    file_id AS fileId,
                    name,
                    description,
                    has_password AS hasPassword,
                    is_in_directory AS isInDirectory,
                    is_directory AS isDirectory,
                    is_deleted AS isDeleted,
                    department_id AS departmentId,
                    create_user_id AS createUserId,
                    parent_file_id AS parentFileId,
                    created_at AS createdAt,
                    updated_at AS updatedAt,
                    deleted_at AS deletedAt
                FROM files
                WHERE is_deleted=:isDeleted
                AND parent_file_id=:parentFileId
                """, resultSetMapping = "FileDTOMapping")
})
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(name = "FileDTOMapping", classes = @ConstructorResult(targetClass = FileDTO.class, columns = {
                @ColumnResult(name = "fileId", type = byte[].class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "hasPassword", type = Boolean.class),
                @ColumnResult(name = "isInDirectory", type = Boolean.class),
                @ColumnResult(name = "isDirectory", type = Boolean.class),
                @ColumnResult(name = "isDeleted", type = Boolean.class),
                @ColumnResult(name = "departmentId", type = byte[].class),
                @ColumnResult(name = "createUserId", type = byte[].class),
                @ColumnResult(name = "parentFileId", type = byte[].class),
                @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                @ColumnResult(name = "updatedAt", type = LocalDateTime.class),
                @ColumnResult(name = "deletedAt", type = LocalDateTime.class),
        })),
        @SqlResultSetMapping(name = "FileDetailDTOMapping", classes = @ConstructorResult(targetClass = FileDetailDTO.class, columns = {
                @ColumnResult(name = "fileId", type = byte[].class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "hasPassword", type = Boolean.class),
                @ColumnResult(name = "isInDirectory", type = Boolean.class),
                @ColumnResult(name = "isDirectory", type = Boolean.class),
                @ColumnResult(name = "isDeleted", type = Boolean.class),
                @ColumnResult(name = "parentFileId", type = byte[].class),
                @ColumnResult(name = "departmentId", type = byte[].class),
                @ColumnResult(name = "departmentName", type = String.class),
                @ColumnResult(name = "createUserId", type = byte[].class),
                @ColumnResult(name = "createUserEmail", type = String.class),
                @ColumnResult(name = "fileDetailSize", type = Integer.class),
                @ColumnResult(name = "fileDetailIsUploaded", type = Boolean.class),
                @ColumnResult(name = "fileDetailIsUploading", type = Boolean.class),
                @ColumnResult(name = "fileDetailIsVersion", type = Boolean.class),
                @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                @ColumnResult(name = "updatedAt", type = LocalDateTime.class),
                @ColumnResult(name = "deletedAt", type = LocalDateTime.class)
        }))
})
public class File {
    @Id
    @Column(name = "file_id", columnDefinition = "VARCHAR(36)")
    private String fileId;

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
    @JoinColumn(name = "parent_file_id", nullable = true, referencedColumnName = "file_id")
    @JsonBackReference
    private File parentFile;

    @OneToMany(mappedBy = "parentFile", cascade = CascadeType.REMOVE)
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
