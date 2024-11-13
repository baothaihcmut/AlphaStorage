package vn.anpha.storage.File.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.Detail.Service.DetailService;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Mapper.FileMapper;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.Folder.Repository.FolderRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class FileService {
    private FileRepository fileRepository;
    private FileMapper fileMapper;
    private AuthoticationService authService;
    private FolderRepository folderRepository;
    private UserOfDepartmentRepository userOfDepartmentRepository;
    private StorageService storageService;
    private DepartmentRepository departmentRepository;
    private CompanyService companyService;
    private DetailService detailService;

    private File uploadAndSaveFile(File file, String bucketName) throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define the desired format (e.g., "yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time as a string
        String formattedDateTime = currentDateTime.format(formatter);
        file.setLink(formattedDateTime.concat(file.getName()));
        String presignedUrl = this.storageService.getPresignUrlForPut(bucketName, file.getLink(), 3);
        file = fileRepository.save(file);
        file.setLink(presignedUrl);
        return file;
    }

    private Department checkPermissionCompanyFile(User user, File file, Boolean hard) {
        Department department;
        if (file.getIsInFolder()) {
            if (file.getFolder().getFolderId() == null) {
                throw new AppException(ErrorCode.FOLDER_ID_REQUIRED);
            }
            Folder folder = folderRepository.findById(file.getFolder().getFolderId())
                    .orElseThrow(() -> new AppException(ErrorCode.FOLDER_NOT_EXIST));
            file.setFolder(folder);
            department = departmentRepository.findById(folder.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        } else {
            if (file.getDepartment().getDepartmentId() == null) {
                throw new AppException(ErrorCode.DEPARTMENT_ID_REQUIRED);
            }
            department = departmentRepository.findById(file.getDepartment().getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
            file.setDepartment(department);
        }
        // check if user is in department
        if (!hard) {
            this.userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
        } else {
            Boolean isValid = this.userOfDepartmentRepository.findManagerOfDepartment(department.getDepartmentId())
                    .stream()
                    .anyMatch((userDepartment) -> userDepartment.getUser().getUserId().equals(user.getUserId()));
            if (!isValid) {
                throw new AppException(ErrorCode.FILE_PERMISSION_NOT_ALLOWED);
            }
        }
        return department;

    }

    private void checkPermissionPersonalFile(User user, File file) {
        if (!file.getCreateBy().getUserId().equals(user.getUserId())) {
            throw new AppException(ErrorCode.FILE_PERMISSION_NOT_ALLOWED);
        }
    }

    @Transactional
    public FileResponse createFile(FileCreationRequest dto) throws Exception {
        File file = fileMapper.toFile(dto);
        User user = authService.getUserByToken();
        file.setCreateBy(user);
        // if file is
        if (file.getIsPersional()) {
            this.detailService.checkSizeAndUpdateSize(user, dto.getFileSize());
            return this.fileMapper.toFileResponse(this.uploadAndSaveFile(file, user.getEmail()));
        }
        // check permission
        Department department = this.checkPermissionCompanyFile(user, file, false);
        // check if company exeed limit
        Company company = this.companyService.updateCompanySize(department.getCompany().getCompanyId(),
                BigInteger.valueOf(file.getFileSize().longValue()));
        return this.fileMapper.toFileResponse(this.uploadAndSaveFile(file, company.getName()));
    }

    public void updateUploadStatus(UUID fileId) {
        File file = fileRepository.findFileByID(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        User user = this.authService.getUserByToken();
        if (file.getIsPersional()) {
            this.checkPermissionPersonalFile(user, file);
        } else {
            this.checkPermissionCompanyFile(user, file, false);
        }
        file.setIsUploaded(true);
        this.fileRepository.save(file);
    }

    public void deleteFileSoft(UUID fileId) {
        User user = this.authService.getUserByToken();
        File file = fileRepository.findFileByID(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        if (file.getIsPersional()) {
            this.checkPermissionPersonalFile(user, file);
        } else {
            this.checkPermissionCompanyFile(user, file, false);
        }
        file.setDeleted(true);
        if (file.getIsInFolder()) {
            file.setPath(this.folderRepository.findFolderPath(file.getFolder().getFolderId()));
        }
    }

    @Transactional
    public void deleteFileHard(UUID fileId) throws Exception {
        User user = this.authService.getUserByToken();
        File file = this.fileRepository.findFileByID(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        String bucketName;
        if (file.getIsPersional()) {
            this.checkPermissionPersonalFile(user, file);
            this.detailService.removeFile(user, file.getFileSize());
            bucketName = user.getEmail();
        } else {
            Department department = this.checkPermissionCompanyFile(user, file, true);
            Company company = this.companyService.removeFileCompany(department.getCompany().getCompanyId(),
                    file.getFileSize());
            bucketName = company.getName();
        }
        this.storageService.removeFile(bucketName, file.getLink());
        this.fileRepository.delete(file);
    }

}
