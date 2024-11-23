package vn.anpha.storage.Version.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.DTO.response.VersionDTO;

@ToString
@Getter
@Setter
@Entity
@Table(name = "versions")
@NamedNativeQueries(value = {
                @NamedNativeQuery(name = "Version.FindVersionById", query = """
                                SELECT
                                    v.version_id AS versionId,
                                    v.link AS link,
                                    v.description AS description,
                                    v.size AS size,
                                    v.file_id AS fileId,
                                    v.update_user_id AS updateUserId,
                                    v.created_at AS createdAt
                                FROM versions v
                                WHERE v.version_id=:versionId
                                LIMIT 1
                                """, resultSetMapping = "VersionDTOMapping"),
                @NamedNativeQuery(name = "Version.FindVersionDetailById", query = """
                                SELECT
                                    v.version_id AS versionId,
                                    v.link as link,
                                    v.description AS description,
                                    v.size AS size,
                                    v.created_at createdAt,
                                    f.file_id AS fileId,
                                    f.name AS fileName,
                                    u.user_id AS updateUserId,
                                    u.email AS updateUserEmail
                                FROM versions v
                                LEFT JOIN files f ON v.file_id = f.file_id
                                LEFT JOIN users u ON v.update_user_id = u.user_id
                                WHERE v.version_id=:versionId
                                LIMIT 1
                                """, resultSetMapping = "VersionDetailDTOMapping"),
                @NamedNativeQuery(name = "Version.FindAllVersionOfFile", query = """
                                SELECT
                                    v.version_id AS versionId,
                                    v.link AS link,
                                    v.description AS description,
                                    v.size AS size,
                                    v.file_id AS fileId,
                                    v.update_user_id AS updateUserId,
                                    v.created_at AS createdAt
                                FROM versions v
                                WHERE v.file_id=:fileId
                                """, resultSetMapping = "VersionDTOMapping"),
})
@SqlResultSetMappings(value = {
                @SqlResultSetMapping(name = "VersionDTOMapping", classes = @ConstructorResult(targetClass = VersionDTO.class, columns = {
                                @ColumnResult(name = "versionId", type = byte[].class),
                                @ColumnResult(name = "link", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "fileId", type = byte[].class),
                                @ColumnResult(name = "updateUserId", type = byte[].class),
                                @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                })),
                @SqlResultSetMapping(name = "VersionDetailDTOMapping", classes = @ConstructorResult(targetClass = VersionDTO.class, columns = {
                                @ColumnResult(name = "versionId", type = byte[].class),
                                @ColumnResult(name = "link", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "fileId", type = byte[].class),
                                @ColumnResult(name = "fileName", type = String.class),
                                @ColumnResult(name = "updateUserId", type = byte[].class),
                                @ColumnResult(name = "updateUserEmail", type = String.class),
                                @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                }))
})
public class Version {
        @Id
        @UuidGenerator(style = UuidGenerator.Style.RANDOM)
        @Column(name = "version_id")
        private UUID versionId;

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
