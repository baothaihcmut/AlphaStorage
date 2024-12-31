package vn.anpha.storage.File.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.DTO.projections.CompanySizeProjection;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.FileDetailDTO;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileDTO;
import vn.anpha.storage.File.DTO.Response.Action;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataLinkDTO;
import vn.anpha.storage.File.Interface.IFileService;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.DTO.response.VersionDTO;
import vn.anpha.storage.Version.Repository.VersionRepository;
import vn.anpha.storage.Version.Service.VersionService;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileService implements IFileService {
        private final FileRepository fileRepository;
        private final CompanyService companyService;
        private final StorageService storageService;
        private final FileDetailRepository fileDetailRepository;
        private final DepartmentRepository departmentRepository;
        private final AuthoticationService authService;
        private final VersionService versionService;
        private final VersionRepository versionRepository;

        @Value("${version.numOfVersion}")
        private Integer numOfVersion;

        @Value("${minio.urlDuration}")
        private Integer urlDuration;

        private FileDetailUploadLinkDTO getPresignUrlAndSave(FileCreationDTO file, String bucket,
                        Boolean isVersion)
                        throws Exception {
                // set file id
                file.setFileId(UUID.randomUUID().toString());
                this.fileRepository.insertFile(file);
                // insert file detail with file_id, bucket name is company id, isuploaded false,
                // link is object name
                String uploadLink = null;
                if (!file.getIsDirectory()) {
                        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucket,
                                        file.getName(), urlDuration);
                        FileDetailCreationDTO fileDetailCreationDTO = file.getFileDetail();
                        fileDetailCreationDTO.setFileId(file.getFileId().toString());
                        fileDetailCreationDTO.setBucketName(bucket);
                        fileDetailCreationDTO.setIsUploaded(false);
                        fileDetailCreationDTO.setIsVersion(isVersion);
                        fileDetailCreationDTO.setLink(uploadRes.getKey());
                        fileDetailCreationDTO.setIsUploading(true);
                        this.fileDetailRepository.insertFileDetail(fileDetailCreationDTO);
                        uploadLink = uploadRes.getValue();
                }
                FileDetailDTO fileDetailDTO = this.fileRepository.findFileDetailById(file.getFileId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                return new FileDetailUploadLinkDTO(fileDetailDTO, file.getIsDirectory() ? null
                                : new FileMetaDataLinkDTO(Action.UPLOAD, null, uploadLink,
                                                urlDuration));
        }

        private void checkFileNameInDirectory(String fileParentId, String name) {
                List<FileDTO> subFiles = this.fileRepository.findAllFileInDirectory(false, fileParentId);
                boolean fileNameExist = subFiles.stream().anyMatch((file) -> file.getName().equals(name));
                if (fileNameExist) {
                        throw new AppException(ErrorCode.FILE_NAME_EXIST_NAME);
                }
        }

        private FileDTO checkFileExist(String fileId) {
                FileDTO file = this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
                return file;
        }

        @Transactional
        public FileDetailUploadLinkDTO uploadFile(FileCreationDTO dto) throws Exception {
                User user = this.authService.getUserByToken();
                // set owner of file
                dto.setCreateUserId(user.getUserId().toString());
                // check parent exist
                if (dto.getIsInDirectory()) {
                        FileDTO parentfileExistProjection = this.checkFileExist(dto.getParentFileId());
                        System.out.println(parentfileExistProjection.getIsDirectory());
                        if (!parentfileExistProjection.getIsDirectory()) {
                                throw new AppException(ErrorCode.DIRECTORY_UNVALID);
                        }
                        // check file name exist in directory
                        this.checkFileNameInDirectory(parentfileExistProjection.getFileId(), dto.getName());
                }
                // check company size
                Department department = this.departmentRepository.findById(UUID.fromString(dto.getDepartmentId()))
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                CompanySizeProjection companySizeProjection = this.companyService
                                .createNewFileCompanySize(UUID.fromString(department.getCompany().getCompanyId()),
                                                dto.getFileDetail().getSize());
                // add to file tag table

                // upload file
                return this.getPresignUrlAndSave(dto,
                                department.getCompany().getCompanyId().toString(),
                                companySizeProjection.getHasVersion());

        }

        @Transactional
        public FileMetaDataDTO announceUploadFile(String fileId, AnnounceUploadDTO annouceUploadDTO) {
                // get user
                User user = this.authService.getUserByToken();
                // Get file detail
                FileMetaDataDTO fileDetailDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));
                // peresist to db
                this.fileDetailRepository.updateUploadStatus(fileId, true, false);
                // if file has version create version
                if (fileDetailDTO.getIsVersion()) {
                        this.versionService.createVersion(fileDetailDTO, annouceUploadDTO.getDescription(), user);
                }

                return this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        }

        public FileMetaDataLinkDTO downloadFile(String fileId) throws Exception {
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));

                if (!fileMetaDataDTO.getIsUploaded()) {
                        throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
                }
                String link = this.storageService.getPresignUrlForGet(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink(),
                                10);

                return new FileMetaDataLinkDTO(Action.DOWNLOAD, fileMetaDataDTO, link, urlDuration);
        }

        @Transactional
        public FileMetaDataLinkDTO updateFile(String fileId, UpdateFileDTO dto) throws Exception {
                // get file info in db
                FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));
                // get department for company info
                Department department = this.departmentRepository.findById(UUID.fromString(fileDTO.getDepartmentId()))
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                // update file size in file_details table
                this.fileDetailRepository.updateFileSize(fileId, dto.getNewFileSize());
                if (fileMetaDataDTO.getIsVersion()) {
                        // if file is versioning
                        // get list of version of file
                        List<VersionDTO> versions = this.versionRepository.findAllVersionOfFile(fileId);
                        if (versions.size() == 0) {
                                throw new AppException(ErrorCode.VERSION_NOT_EXIST);
                        }
                        if (versions.size() < numOfVersion) {
                                // update in company without delete any version
                                this.companyService.updateFileCompanySize(
                                                UUID.fromString(department.getCompany().getCompanyId()),
                                                fileMetaDataDTO.getSize(), dto.getNewFileSize(),
                                                fileMetaDataDTO.getIsVersion(), 0);
                        } else {
                                // file earliest version for delete
                                VersionDTO deletedVersions = versions.stream()
                                                .min(Comparator.comparing(VersionDTO::getCreatedAt))
                                                .orElseThrow(() -> new AppException(ErrorCode.VERSION_NOT_EXIST));
                                // update in company
                                this.companyService.updateFileCompanySize(
                                                UUID.fromString(department.getCompany().getCompanyId()),
                                                fileMetaDataDTO.getSize(), dto.getNewFileSize(), true,
                                                deletedVersions.getSize());
                                // delete version in db
                                this.versionRepository.deleteVersion(deletedVersions.getVersionId());
                                // delete version in storage
                                this.storageService.removeVersionOfFile(fileMetaDataDTO.getBucketName(),
                                                fileMetaDataDTO.getLink(),
                                                deletedVersions.getLink());

                        }
                } else {
                        // if file is not versioning
                        this.companyService.updateFileCompanySize(
                                        UUID.fromString(department.getCompany().getCompanyId()),
                                        fileMetaDataDTO.getSize(),
                                        dto.getNewFileSize(), false, 0);
                }
                // setting isuploading to true and is uploaded to true
                this.fileDetailRepository.updateUploadStatus(fileId, true, true);
                // get presignlink for update
                String uploadREs = this.storageService.getPresignUrlForUpdate(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink(), 10);
                return new FileMetaDataLinkDTO(
                                Action.UPDATE,
                                this.fileDetailRepository.findFileMetaDataById(fileId)
                                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST)),
                                uploadREs,
                                urlDuration);
        }

}