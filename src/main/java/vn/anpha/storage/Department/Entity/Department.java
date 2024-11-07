package vn.anpha.storage.Department.Entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "department_id")
    private UUID departmentId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, referencedColumnName = "company_id")
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference // Đánh dấu là thực thể cha
    private List<DepartmentUser> employees;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Folder> folders;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<File> files;

}
