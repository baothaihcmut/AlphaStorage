package vn.anpha.storage.Company.Entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

@ToString
@Getter
@Setter
@Entity
@Table(name = "companys")
public class Company {
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
    @JsonBackReference
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
