package vn.anpha.storage.User_company.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
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
@IdClass(UserOfCompany.UserOfCompanyId.class) // Sử dụng @IdClass để định nghĩa khóa chính tổng hợp
public class UserOfCompany {
    @Id
    @ManyToOne
    @JoinColumn(name = "employee_Id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, referencedColumnName = "company_id")
    @JsonBackReference
    private Company company;

    private boolean status;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Lớp khóa chính tổng hợp
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class UserOfCompanyId implements Serializable {
        private UUID employee; // ID của nhân viên
        private UUID company; // ID của công ty

        public UserOfCompanyId() {
        }

        public UserOfCompanyId(UUID employee, UUID company) {
            this.employee = employee;
            this.company = company;
        }
    }
}