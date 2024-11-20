package vn.anpha.storage.File.Interface;

public interface IWriteFilePermissionService {
    boolean hasPermissionManager(String fileId);

    boolean hasPermission(String fileId);
}
