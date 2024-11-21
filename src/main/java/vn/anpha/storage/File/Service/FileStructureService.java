package vn.anpha.storage.File.Service;

import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.DTO.Projection.FileProjection;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoRequest;
import vn.anpha.storage.File.DTO.Request.MoveFileRequest;
import vn.anpha.storage.File.DTO.Request.RecoverFileRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Mapper.FileMapper;
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
    private final FileMapper fileMapper;
    private final AuthoticationService authService;
    private final DepartmentRepository departmentRepository;
    private final StorageService storageService;
    private final FileDetailRepository fileDetailRepository;
    private final CompanyService companyService;

    private FileProjection getPresignUrlAndSave(File file, String bucket) throws Exception {
        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucket,
                file.getName(), 3);
        // set file entity field
        file.getFileDetail().setLink(uploadRes.getKey());
        file.getFileDetail().setBucketName(bucket);
        file.setFileId(UUID.randomUUID());
        file.setPassword(file.getFileId().toString());
        this.fileRepository.insertFile(file);
        // set field detail field
        System.out.println(file.getFileId());
        file.getFileDetail().setFileId(file.getFileId());
        file.getFileDetail().setIsVersion(false);
        file.getFileDetail().setIsUploaded(false);
        this.fileDetailRepository.insertFileDetail(file.getFileDetail());
        file.getFileDetail().setLink(uploadRes.getValue());
        return this.fileRepository.findFileDetailById(file.getFileId(), false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public FileProjection createFile(FileCreationRequest dto) throws Exception {
        File file = this.fileMapper.toFile(dto);
        User user = this.authService.getUserByToken();
        // set owner of file
        file.setCreateBy(user);

        // check parent exist
        if (dto.getIsInDirectory()) {
            FileExistProjection parentfileExistProjection = this.checkFileExist(file.getParentFile().getFileId());
            if (parentfileExistProjection.getIsDirectory()) {
                throw new AppException(ErrorCode.DIRECTORY_UNVALID);
            }
        }
        // add to File tags table
        // update company size
        Department department = this.departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        // check company size
        this.companyService.createNewFileCompanySize(department.getCompany().getCompanyId(),
                file.getFileDetail().getSize());
        // upload file
        return this.getPresignUrlAndSave(file, department.getCompany().getCompanyId().toString());
    }

    @Transactional
    public FileResponse updateFileInfo(UUID fileId, FileUpdateInfoRequest fileUpdateInfoRequest) {
        File file = this.fileMapper.toFile(fileUpdateInfoRequest);
        file.setFileId(fileId);
        // check tag exist

        // update file
        file = this.fileRepository.save(file);
        return this.fileMapper.toFileResponse(file);
    }

    @Transactional
    public void deleteFileSoft(UUID fileId) {
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        if (fileExistProjection.getIsDirectory()) {
            this.fileRepository.updateDeleteChildFile(fileId, true, LocalDateTime.now());
        } else {
            this.fileRepository.updateDeleteFile(fileId, true, LocalDateTime.now());
        }
    }

    @Transactional
    public void moveFile(UUID fileId, MoveFileRequest moveFileRequest) {
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        // check directory
        FileExistProjection newDir = this.checkFileExist(moveFileRequest.getNewFileId());
        if (!newDir.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }
        // Check if new Dir out of department
        if (!fileExistProjection.getDepartmentId().equals(newDir.getDepartmentId())) {
            throw new AppException(ErrorCode.NEW_DIRECTORY_NOT_IN_DEPARTMENT);
        }
        this.fileRepository.moveFile(fileId, newDir.getFileId());
    }

    @Transactional
    public void recoverFile(UUID fileId, RecoverFileRequest recoverFileRequest) {
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));
        FileExistProjection parentFileExistProjection = this.fileRepository
                .findFileById(recoverFileRequest.getRecoverParentFileId(), true)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        if (parentFileExistProjection.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }
        if (fileExistProjection.getIsDirectory()) {
            this.fileRepository.updateDeleteChildFile(fileId, false, null);
        } else {
            this.fileRepository.updateDeleteFile(fileId, false, null);
        }

    }

    private FileExistProjection checkFileExist(UUID fileId) {
        FileExistProjection file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }
}
