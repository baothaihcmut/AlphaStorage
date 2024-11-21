package vn.anpha.storage.File.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.File.DTO.Projection.FileDetailProjection;
import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final CompanyService companyService;
    private final StorageService storageService;
    private final FileDetailRepository fileDetailRepository;

    @Transactional
    public void updateUploadStatus(UUID fileId, Boolean status) throws Exception {
        // check if file is directory
        FileDetailProjection fileDetailProjection = this.fileDetailRepository.findFileDetailById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        this.companyService.createNewFileCompanySize(UUID.fromString(fileDetailProjection.getBucketName()),
                fileDetailProjection.getSize());

        this.fileDetailRepository.updateUploadStatus(fileId, status);
    }

    @Transactional
    public void deleteHard(UUID fileId) throws Exception {
        FileExistProjection fileExistProjection = this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));
        List<FileDetailProjection> links = new ArrayList<>();
        if (fileExistProjection.getIsDirectory()) {
            links.addAll(this.fileDetailRepository.findLinkOfAllChild(fileId));
        } else {
            links.add(this.fileDetailRepository.findLinkOfFile(fileId)
                    .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST)));
        }

        File file = new File();
        file.setFileId(fileId);
        this.fileRepository.delete(file);
        for (FileDetailProjection fileDetail : links) {
            this.companyService.removeFileCompany(UUID.fromString(fileDetail.getBucketName()), fileDetail.getSize());
            this.storageService.removeFile(fileDetail.getBucketName(), fileDetail.getLink());
        }
    }

    @Transactional
    public FileDetailProjection updateFile() {
        return null;
    }
}