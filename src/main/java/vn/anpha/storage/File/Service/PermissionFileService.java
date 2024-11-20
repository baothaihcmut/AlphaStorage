package vn.anpha.storage.File.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.Interface.IReadFilePermissionService;
import vn.anpha.storage.File.Interface.IWriteFilePermissionService;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class PermissionFileService implements IReadFilePermissionService, IWriteFilePermissionService {
    private final AuthoticationService authService;
    private final FileRepository fileRepository;
    private final DepartmentRepository departmentRepository;
    private final UserOfDepartmentRepository userOfDepartmentRepository;

    private FileExistProjection checkFileExist(String fileId) {
        return this.fileRepository.findFileById(UUID.fromString(fileId), false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    public boolean hasPermissionManager(String fileId) {
        User user = this.authService.getUserByToken();
        FileExistProjection fileExistProjection = this.checkFileExist(fileId);
        Department department = this.departmentRepository
                .findDepartmentById(fileExistProjection.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        this.userOfDepartmentRepository.findManagerOfDepartment(department.getDepartmentId()).stream()
                .anyMatch((userDepartment) -> userDepartment.getUser().getUserId().equals(user.getUserId()));

        return true;
    }

    public boolean hasPermission(String fileId) {
        User user = this.authService.getUserByToken();
        FileExistProjection fileExistProjection = this.checkFileExist(fileId);
        Department department = this.departmentRepository
                .findDepartmentById(fileExistProjection.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        this.userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
        return true;
    }
}
