package vn.anpha.storage.Department.DTO.projection;

public interface DepartmentDTO {
    String getDepartmentId();

    String getName();

    String getDescription();

    Integer getTotalSize();

    String getCompanyId();

    String getParentDepartmentId();

}
