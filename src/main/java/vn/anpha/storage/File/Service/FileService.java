package vn.anpha.storage.File.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Company.interfaceCompany.CompanyInterface;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.Action;
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Projection.StorageDetailDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileContentDTO;
import vn.anpha.storage.File.DTO.Response.CreateFileResponse;
import vn.anpha.storage.File.DTO.Response.UpdateFileContentResponse;
import vn.anpha.storage.File.Interface.IFileService;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.DTO.projection.VersionDTO;
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

        private String getObjectName(String fileName) {
                LocalDateTime now = LocalDateTime.now();

                // Format the date-time to a string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedTime = now.format(formatter);
                return String.format("%s%s", formattedTime, fileName);
        }

        private CreateFileResponse saveFile(FileCreationDTO file, String bucket,
                        Boolean isVersion)
                        throws Exception {
                // set file id
                file.setFileId(UUID.randomUUID().toString());
                file.setTotalSize(file.getIsDirectory() ? 0 : file.getFileDetail().getSize());
                this.fileRepository.insertFile(file);
                // insert file detail with file_id, bucket name is company id, isuploaded false,
                // link is object name
                if (!file.getIsDirectory()) {
                        FileDetailCreationDTO fileDetailCreationDTO = file.getFileDetail();
                        fileDetailCreationDTO.setFileId(file.getFileId().toString());
                        fileDetailCreationDTO.setBucketName(bucket);
                        fileDetailCreationDTO.setIsUploaded(false);
                        fileDetailCreationDTO.setIsVersion(isVersion);
                        fileDetailCreationDTO.setLink(this.getObjectName(file.getName()));
                        fileDetailCreationDTO.setIsUploading(true);
                        this.fileDetailRepository.insertFileDetail(fileDetailCreationDTO);
                }
                FileDTO fileDetailDTO = this.fileRepository.findFileById(file.getFileId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                if (!file.getIsDirectory()) {
                        String uploadRes = this.storageService.getPresignUrlForPut(bucket, file.getName(),
                                        this.urlDuration);
                        StorageDetailDTO storageDetailDTO = new StorageDetailDTO(
                                        Action.UPLOAD,
                                        uploadRes,
                                        file.getFileDetail().getMimeType(),
                                        file.getFileDetail().getSize(),
                                        this.urlDuration);
                        return new CreateFileResponse(fileDetailDTO, storageDetailDTO);
                } else {
                        return new CreateFileResponse(fileDetailDTO, null);
                }
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
        public CreateFileResponse createFile(FileCreationDTO dto) throws Exception {
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
                String bucketName = null;
                Boolean isVersion = false;
                if (!dto.getIsDirectory()) {
                        DepartmentDTO department = this.departmentRepository
                                        .findDepartmentById(dto.getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                        CompanyInterface companySizeProjection = this.companyService
                                        .createNewFileCompanySize(department.getCompanyId(),
                                                        dto.getFileDetail().getSize());
                        bucketName = companySizeProjection.getCompanyId();
                        isVersion = companySizeProjection.getHasVersion();
                }
                // update parent total size
                if (!dto.getIsDirectory() && dto.getFileDetail() != null && dto.getIsInDirectory()
                                && dto.getParentFileId() != null) {
                        this.fileRepository.updateTotalSizeFileSystem(dto.getParentFileId(),
                                        dto.getFileDetail().getSize());
                }
                // upload file
                CreateFileResponse res = this.saveFile(dto,
                                bucketName,
                                isVersion);
                if (!dto.getIsDirectory() && dto.getFileDetail() != null && dto.getIsInDirectory()
                                && dto.getParentFileId() != null) {
                        this.fileRepository.updateTotalSizeFileSystem(res.getFile().getFileId(),
                                        dto.getFileDetail().getSize());
                }
                return res;

        }

        @Transactional
        public FileMetaDataDTO announceUploadFile(String fileId, AnnounceUploadDTO annouceUploadDTO) {
                // get user
                // Get file detail
                FileMetaDataDTO fileDetailDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));
                // peresist to db
                this.fileDetailRepository.updateUploadStatus(fileId, true, false);
                // if file has version create version
                if (fileDetailDTO.getIsVersion()) {
                        this.versionService.createVersion(fileDetailDTO.getFileId(), annouceUploadDTO.getDescription());
                }
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));

                return fileMetaDataDTO;
        }

        public StorageDetailDTO downloadFile(String fileId) throws Exception {
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));

                if (!fileMetaDataDTO.getIsUploaded()) {
                        throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
                }
                String link = this.storageService.getPresignUrlForGet(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink(),
                                10);

                return new StorageDetailDTO(
                                Action.DOWNLOAD,
                                link,
                                fileMetaDataDTO.getMimeType(),
                                fileMetaDataDTO.getSize(),
                                urlDuration);
        }

        @Transactional
        public UpdateFileContentResponse updateFileContent(String fileId, UpdateFileContentDTO dto) throws Exception {
                // get file info in db
                FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));
                if (!fileMetaDataDTO.getIsUploaded()) {
                        throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
                }
                if (fileMetaDataDTO.getIsUploading()) {
                        throw new AppException(ErrorCode.FILE_IS_UPLOADING);
                }
                // get department for company info
                DepartmentDTO department = this.departmentRepository
                                .findDepartmentById(fileDTO.getDepartmentId())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                // update file size in file_details table
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
                                                department.getCompanyId(),
                                                fileMetaDataDTO.getSize(), dto.getSize(),
                                                fileMetaDataDTO.getIsVersion(), 0);
                        } else {
                                // file earliest version for delete
                                VersionDTO deletedVersions = versions.stream()
                                                .min(Comparator.comparing(VersionDTO::getCreatedAt))
                                                .orElseThrow(() -> new AppException(ErrorCode.VERSION_NOT_EXIST));
                                // update in company
                                this.companyService.updateFileCompanySize(
                                                department.getCompanyId(),
                                                fileMetaDataDTO.getSize(), dto.getSize(), true,
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
                                        department.getCompanyId(),
                                        fileMetaDataDTO.getSize(),
                                        dto.getSize(), false, 0);
                }
                // setting isuploading to true and is uploaded to true
                dto.setIsUploaded(true);
                dto.setIsUploading(true);
                this.fileDetailRepository.updateFileMetaData(fileId, dto);
                // get presignlink for update
                String uploadREs = this.storageService.getPresignUrlForUpdate(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink(), 10);
                FileMetaDataDTO fileMetaDataDTORes = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));
                StorageDetailDTO storageDetailDTO = new StorageDetailDTO(
                                Action.UPDATE,
                                uploadREs,
                                fileMetaDataDTO.getMimeType(),
                                fileMetaDataDTO.getSize(),
                                urlDuration);
                return new UpdateFileContentResponse(fileMetaDataDTORes, storageDetailDTO);
        }

        public FileDTO getFileDetail(String fileId) {
                return this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        }

        public List<FileDTO> findAllFileInDirectory(String parentFileId) {
                // check if parentFile exist
                FileDTO parentFile = this.fileRepository.findFileById(parentFileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
                if (!parentFile.getIsDirectory()) {
                        throw new AppException(ErrorCode.DIRECTORY_UNVALID);
                }
                return this.fileRepository.findAllFileInDirectory(false, parentFileId);
        }

        @Transactional
        void hardDeleteFile(String fileId) {
                // get file in db
                FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                // check if file in trash
                if (!fileDTO.getIsDeleted()) {
                        throw new AppException(ErrorCode.FILE_NOT_IN_TRASH);
                }
                
        }

}