package vn.anpha.storage.User_Department.Entity;

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
import lombok.Data;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User.Entity.User;

@Data
@Entity
@Table(name = "department_of_user")
public class DepartmentUser {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    @JsonBackReference
    private User user;

    @Column(columnDefinition = " default false")
    private boolean isManager;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false, referencedColumnName = "department_id")
    @JsonBackReference
    private Department department;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}