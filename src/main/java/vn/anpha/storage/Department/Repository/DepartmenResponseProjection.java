package vn.anpha.storage.Department.Repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface DepartmenResponseProjection {

    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.departmentId)}")
    UUID getDepartmentId();

    String getName();

}