package vn.anpha.storage.User_Department.DTO.projection;

public class DepartmentUserDtoImpl implements DepartmentUserDto {

     public String userId;
     public String departmentId;
     public Boolean isManager;

     // Constructor để khởi tạo giá trị
     public DepartmentUserDtoImpl(String userId, String departmentId, Boolean isManager) {
          this.userId = userId;
          this.departmentId = departmentId;
          this.isManager = isManager;
     }

     // Getter methods từ interface
     @Override
     public String getUserId() {
          return userId;
     }

     @Override
     public String getDepartmentId() {
          return departmentId;
     }

     @Override
     public Boolean getIsManager() {
          return isManager;
     }

     // Setter methods
     public void setUserId(String userId) {
          this.userId = userId;
     }

     public void setDepartmentId(String departmentId) {
          this.departmentId = departmentId;
     }

     public void setIsManager(Boolean isManager) {
          this.isManager = isManager;
     }
}
