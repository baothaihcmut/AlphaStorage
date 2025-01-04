package vn.anpha.storage.Department.DTO.projection;

import java.util.ArrayList;
import java.util.List;

public class TreeDepartment {
     private DepartmentDTO department;
     private List<TreeDepartment> subDepartments = new ArrayList<>();

     // Constructor, getters, and setters
     public TreeDepartment(DepartmentDTO department) {
          this.department = department;
     }

     public DepartmentDTO getDepartment() {
          return department;
     }

     public void setDepartment(DepartmentDTO department) {
          this.department = department;
     }

     public List<TreeDepartment> getSubDepartments() {
          return subDepartments;
     }

     public void addSubDepartment(TreeDepartment subDepartment) {
          this.subDepartments.add(subDepartment);
     }
}
