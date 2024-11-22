package vn.anpha.storage.File.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileStructureService implements IFileStructureService {
    private final FileRepository fileRepository;
    private final AuthoticationService authService;
    private final DepartmentRepository departmentRepository;
    private final StorageService storageService;
    private final FileDetailRepository fileDetailRepository;
    private final CompanyService companyService;

    private SimpleEntry<FileDetailDTO, String> getPresignUrlAndSave(FileCreationDTO file, String bucket)
            throws Exception {
        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucket,
                file.getName(), 3);
        // set file id
        file.setFileId(UUID.randomUUID());
        this.fileRepository.insertFile(file);
        // set field detail field
        FileDetailCreationDTO fileDetailCreationDTO = file.getFileDetail();
        fileDetailCreationDTO.setFileId(file.getFileId());
        fileDetailCreationDTO.setBucketName(bucket);
        fileDetailCreationDTO.setIsUploaded(false);
        fileDetailCreationDTO.setIsVersion(false);
        fileDetailCreationDTO.setLink(uploadRes.getKey());
        this.fileDetailRepository.insertFileDetail(file.getFileDetail());
        FileDetailDTO fileDetailDTO = this.fileRepository.findFileDetailById(file.getFileId(), false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        return new SimpleEntry<>(fileDetailDTO, uploadRes.getValue());
    }

    private void checkFileNameInDirectory(UUID fileParentId, String name) {
        List<FileDTO> subFiles = this.fileRepository.findAllFileInDirectory(false, fileParentId);
        boolean fileNameExist = subFiles.stream().anyMatch((file) -> file.getName().equals(name));
        if (fileNameExist) {
            throw new AppException(ErrorCode.FILE_NAME_EXIST_NAME);
        }
    }

    @Transactional
    public FileDetailUploadLinkDTO createFile(FileCreationDTO dto) throws Exception {
        User user = this.authService.getUserByToken();
        // set owner of file
        dto.setCreateUserId(user.getUserId());
        // check parent exist
        if (dto.getIsInDirectory()) {
            FileDTO parentfileExistProjection = this.checkFileExist(dto.getParentFileId());
            if (parentfileExistProjection.getIsDirectory()) {
                throw new AppException(ErrorCode.DIRECTORY_UNVALID);
            }
            // check file name exist in directory
            this.checkFileNameInDirectory(parentfileExistProjection.getFileId(), dto.getName());
        }
        // add to File tags table
        // update company size
        Department department = this.departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        // check company size
        this.companyService.createNewFileCompanySize(department.getCompany().getCompanyId(),
                dto.getFileDetail().getSize());
        // upload file
        SimpleEntry<FileDetailDTO, String> res = this.getPresignUrlAndSave(dto,
                department.getCompany().getCompanyId().toString());
        return new FileDetailUploadLinkDTO(res.getKey(), res.getValue());

    }

    @Transactional
    public FileDetailDTO updateFileInfo(UUID fileId, FileUpdateInfoDTO fileUpdateInfoRequest) {
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        if (fileDTO.getIsDirectory() && fileUpdateInfoRequest.getName() != null) {
            this.checkFileNameInDirectory(fileDTO.getParentFileId(), fileUpdateInfoRequest.getName());
        }
        this.fileRepository.updateFile(fileId, fileUpdateInfoRequest);
        return this.fileRepository.findFileDetailById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public void deleteFileSoft(UUID fileId) {
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        this.fileRepository.softDeleteFile(fileId);
        if (fileDTO.getIsDirectory()) {
            this.fileRepository.softDeleteChild(fileId);
        }
    }

    @Transactional
    public void moveFile(UUID fileId, MoveFileDTO moveFileRequest) {
        FileDTO file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));

        // check directory
        FileDTO newDir = this.checkFileExist(moveFileRequest.getNewDirectoryId());
        if (!newDir.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }

        // Check if new Dir out of department
        if (!file.getDepartmentId().equals(newDir.getDepartmentId())) {
            throw new AppException(ErrorCode.NEW_DIRECTORY_NOT_IN_DEPARTMENT);
        }

        // check name exist in new dir
        this.checkFileNameInDirectory(newDir.getFileId(), file.getName());

        // update in db
        this.fileRepository.moveFile(fileId, moveFileRequest);
    }

    @Transactional
    public FileDetailDTO recoverFile(UUID fileId, RecoverFileDTO recoverFileRequest) {
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));
        FileDTO parentFileExistProjection = this.fileRepository
                .findFileById(recoverFileRequest.getRecoverDirectoryId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        if (parentFileExistProjection.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }
        this.fileRepository.recoverFile(fileId, recoverFileRequest);
        if (fileDTO.getIsDirectory()) {
            this.fileRepository.recoverChild(fileId);
        }
        return this.fileRepository.findFileDetailById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));

    }

    private FileDTO checkFileExist(UUID fileId) {
        FileDTO file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }
}
