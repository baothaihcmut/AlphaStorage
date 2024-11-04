package vn.anpha.storage.User_company.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

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
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.User.Entity.User;

@ToString
@Getter
@Setter
@Entity
@Table(name = "usersOfCompany")
public class UserOfCompany {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_Id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User employee;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", nullable = true)
    @JsonBackReference
    private Company company;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
