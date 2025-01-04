package vn.anpha.storage.File.Service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class PermissionFileService {
        private final AuthoticationService authService;
        private final FileRepository fileRepository;
        private final UserOfDepartmentRepository userOfDepartmentRepository;

        private FileDTO checkFileExist(String fileId) {
                return this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        }

        public boolean hasDepartmentPermission(String departmentId) {
                User user = this.authService.getUserByToken();
                this.userOfDepartmentRepository.findUserOfDepartment(
                                user.getUserId(),
                                departmentId)
                                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
                return true;
        }

        public boolean hasRecoverPermission(String fileId) {
                User user = this.authService.getUserByToken();
                FileDTO fileDTO = this.fileRepository.findFileById(fileId, true)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));
                this.userOfDepartmentRepository.findManagerOfDepartment(
                                fileDTO.getDepartmentId()).stream()
                                .anyMatch((userDepartment) -> userDepartment.getUserId()
                                                .equals(user.getUserId()));
                return true;
        }

        public boolean hasPermission(String fileId) {
                User user = this.authService.getUserByToken();
                FileDTO fileExistProjection = this.checkFileExist(fileId);
                this.userOfDepartmentRepository.findUserOfDepartment(
                                user.getUserId(),
                                fileExistProjection.getDepartmentId())
                                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
                return true;
        }
}
