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
import vn.anpha.storage.File.DTO.Response.FileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataLinkDTO;
import vn.anpha.storage.File.Interface.IFileService;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
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

    private SimpleEntry<FileDetailDTO, String> getPresignUrlAndSave(FileCreationDTO file, String bucket)
            throws Exception {
        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucket,
                file.getName(), 3);
        // set file id
        file.setFileId(UUID.randomUUID());
        this.fileRepository.insertFile(file);
        // set field detail field
        if (!file.getIsDirectory()) {
            FileDetailCreationDTO fileDetailCreationDTO = file.getFileDetail();
            fileDetailCreationDTO.setFileId(file.getFileId());
            fileDetailCreationDTO.setBucketName(bucket);
            fileDetailCreationDTO.setIsUploaded(false);
            fileDetailCreationDTO.setIsVersion(false);
            fileDetailCreationDTO.setLink(uploadRes.getKey());
            this.fileDetailRepository.insertFileDetail(file.getFileDetail());
        }
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

    private FileDTO checkFileExist(UUID fileId) {
        FileDTO file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }

    @Transactional
    public FileDetailUploadLinkDTO uploadFile(FileCreationDTO dto) throws Exception {
        User user = this.authService.getUserByToken();
        // set owner of file
        dto.setCreateUserId(user.getUserId());
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
        Department department = this.departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        // update file size and check if company have version file
        if (!dto.getIsDirectory()) {
            this.companyService.createNewFileCompanySize(department.getCompany().getCompanyId(),
                    dto.getFileDetail().getSize());
        }
        // if file has version create in version table

        // add to file tag table

        // upload file
        SimpleEntry<FileDetailDTO, String> res = this.getPresignUrlAndSave(dto,
                department.getCompany().getCompanyId().toString());
        return new FileDetailUploadLinkDTO(res.getKey(), res.getValue());

    }

    public FileMetaDataLinkDTO downloadFile(UUID fileId) throws Exception {
        FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST_OR_NOT_FILE));

        if (!fileMetaDataDTO.getIsUploaded()) {
            throw new AppException(ErrorCode.FILE_NOT_UPLOAD);
        }
        String link = this.storageService.getPresignUrlForGet(fileMetaDataDTO.getBucketName(),
                fileMetaDataDTO.getLink(),
                10);

        return new FileMetaDataLinkDTO(fileMetaDataDTO, link, 10);
    }

}