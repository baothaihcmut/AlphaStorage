package vn.anpha.storage.Department.Repository;

import java.util.UUID;

import jakarta.persistence.Convert;
import vn.anpha.storage.config.UUIDConverter;

public interface DepartmenResponseProjection {
    // @Convert(converter = UUIDConverter.class)
    byte[] getDepartmentId(); // Changed from UUID to String

    String getName();

}