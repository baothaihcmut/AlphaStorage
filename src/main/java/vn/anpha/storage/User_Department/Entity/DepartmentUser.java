package vn.anpha.storage.User_Department.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User.Entity.User;

@ToString
@Getter
@Setter
@Entity
@Data
@Table(name = "department_of_user")
@IdClass(DepartmentUser.DepartmentUserId.class)
public class DepartmentUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User user;

    private boolean isManager;

    @Id
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false, referencedColumnName = "department_id")
    @JsonBackReference
    private Department department;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private LocalDateTime updatedAt;

    public DepartmentUser(User user, boolean isManager, Department department) {
        this.user = user;
        this.isManager = isManager;
        this.department = department;
    }

    public DepartmentUser() {
    }

    // Lớp khóa chính tổng hợp
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class DepartmentUserId implements Serializable {
        private String user; // ID của nhân viên
        private String department; // ID của công ty

        public DepartmentUserId() {
        }

        public DepartmentUserId(String user, String department) {
            this.user = user;
            this.department = department;
        }
    }
}