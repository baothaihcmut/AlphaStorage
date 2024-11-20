package vn.anpha.storage.File.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Repository.CompanyRepository;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoRequest;
import vn.anpha.storage.File.DTO.Request.MoveFileRequest;
import vn.anpha.storage.File.DTO.Request.RecoverFileRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Entity.FileDetail;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Mapper.FileDetailMapper;
import vn.anpha.storage.File.Mapper.FileMapper;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileService implements IFileStructureService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final AuthoticationService authService;
    private final DepartmentRepository departmentRepository;
    private final CompanyService companyService;
    private final StorageService storageService;
    private final FileDetailRepository fileDetailRepository;
    private final FileDetailMapper fileDetailMapper;
    private final CompanyRepository companyRepository;

    private Company updateAndCheckCompanySize(File file, UUID departmentId) {
        Department department = this.departmentRepository.findDepartmentById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        Company company = this.companyService.updateCompanySize(department.getCompany().getCompanyId(),
                BigInteger.valueOf(file.getFileDetail().getSize().longValue()));
        return company;
    }

    private FileExistProjection checkFileExist(UUID fileId) {
        FileExistProjection file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }

    private String getBucketOfFile(FileExistProjection fileExistProjection) {
        Department department = this.departmentRepository.findById(fileExistProjection.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        return this.companyRepository.findById(department.getCompany().getCompanyId())
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED)).getName();
    }

    private File getPresignUrlAndSave(File file, String bucket) throws Exception {
        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucket,
                file.getName(), 3);
        file.getFileDetail().setLink(uploadRes.getKey());
        // insert file and file detail
        file.getFileDetail().setFile(file);
        file = this.fileRepository.save(file);
        file.getFileDetail().setLink(uploadRes.getValue());
        return file;
    }

    private String getPresignLinkForGet(FileDetail fileDetail, String bucket) throws Exception {
        return this.storageService.getPresignUrlForGet(bucket, fileDetail.getLink(), 5);
    }

    @Transactional
    public FileResponse createFile(FileCreationRequest dto) throws Exception {
        File file = this.fileMapper.toFile(dto);
        User user = this.authService.getUserByToken();
        // set owner of file
        file.setCreateBy(user);

        // check parent exist
        FileExistProjection parentfileExistProjection = this.checkFileExist(file.getParentFile().getFileId());
        if (parentfileExistProjection.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }
        // add to File tags table
        // update company size
        Company company = this.updateAndCheckCompanySize(file, dto.getDepartmentId());
        // upload file
        File uploadFile = this.getPresignUrlAndSave(file, company.getName());
        return this.fileMapper.toFileResponse(uploadFile);
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

    @Transactional
    public void updateUploadStatus(UUID fileId, Boolean status) throws Exception {
        // check if file is directory
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        if (fileExistProjection.getIsDirectory()) {
            throw new AppException(ErrorCode.FILE_IS_DIRECTORY);
        }
        this.fileDetailRepository.updateUploadStatus(fileId, status);
    }

    @Transactional
    public void deleteHard(UUID fileId) throws Exception {
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));
        List<String> links = new ArrayList<>();
        if (fileExistProjection.getIsDirectory()) {
            links.addAll(this.fileDetailRepository.findLinkOfAllChild(fileId));
        } else {
            links.add(this.fileDetailRepository.findLinkOfFile(fileId)
                    .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST)));
        }
        File file = new File();
        String bucketName = this.getBucketOfFile(fileExistProjection);
        file.setFileId(fileId);
        this.fileRepository.delete(file);
        for (String link : links) {
            this.storageService.removeFile(bucketName, link);
        }
    }
}