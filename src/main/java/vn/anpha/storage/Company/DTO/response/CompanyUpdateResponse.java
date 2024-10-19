package vn.anpha.storage.Company.DTO.response;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyUpdateResponse {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, columnDefinition = "Text")
    private String description;

    private BigInteger total_size;
    private BigInteger limit_size;

    @ManyToOne
    @JoinColumn(name = "created_By")
    private User createBy;

    @OneToMany(mappedBy = "CompanyId", cascade = CascadeType.REMOVE, orphanRemoval =  true)
    private Set<UserOfCompany> UserOfCompanys;

    @OneToMany(mappedBy = "CompanyId")
    private Set<Folder> Folders;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
