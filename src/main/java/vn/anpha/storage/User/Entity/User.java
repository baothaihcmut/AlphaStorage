package vn.anpha.storage.User.Entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Detail.Entity.DetailUser;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.Role.Entity.Role;
import vn.anpha.storage.User_Folder.Entity.FolderOfUser;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

@ToString
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private String address;
    private String phone;

    @Column(nullable = true, unique = true, columnDefinition = "Text")
    private String refreshToken;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference // Đánh dấu là thực thể cha
    private DetailUser details;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonManagedReference
    private Role role;

    @OneToMany(mappedBy = "createBy", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Company> companyOwn;

    @OneToMany(mappedBy = "EmployeeId", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<UserOfCompany> UserOfCompanys;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<File> ownFiles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private Set<FolderOfUser> ownFolders;
}
