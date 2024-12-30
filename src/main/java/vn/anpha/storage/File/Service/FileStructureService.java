package vn.anpha.storage.File.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileStructureService implements IFileStructureService {
    private final FileRepository fileRepository;

    private void checkFileNameInDirectory(UUID fileParentId, String name) {
        List<FileDTO> subFiles = this.fileRepository.findAllFileInDirectory(false, fileParentId);
        boolean fileNameExist = subFiles.stream().anyMatch((file) -> file.getName().equals(name));
        if (fileNameExist) {
            throw new AppException(ErrorCode.FILE_NAME_EXIST_NAME);
        }
    }

    @Transactional
    public FileDetailDTO updateFileInfo(UUID fileId, FileUpdateInfoDTO fileUpdateInfoRequest) {
        // get file in db
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        // if update name check if in directory have exist name
        if (fileDTO.getIsDirectory() && fileUpdateInfoRequest.getName() != null) {
            this.checkFileNameInDirectory(fileDTO.getParentFileId(), fileUpdateInfoRequest.getName());
        }
        // update file in db
        this.fileRepository.updateFile(fileId, fileUpdateInfoRequest);
        // response file info
        return this.fileRepository.findFileDetailById(fileId.toString(), false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public void deleteFileSoft(UUID fileId) {
        // get file in db
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        // set isDeleted to true
        this.fileRepository.softDeleteFile(fileId);

        // if file have field set child deleted
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
        // get file in db
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_IN_TRASH));

        // check if parent file exist
        FileDTO parentFileExistProjection = this.fileRepository
                .findFileById(recoverFileRequest.getRecoverDirectoryId(), false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        // check if parent file is directory
        if (!parentFileExistProjection.getIsDirectory()) {
            throw new AppException(ErrorCode.DIRECTORY_UNVALID);
        }
        // recover file
        this.fileRepository.recoverFile(fileId, recoverFileRequest);

        // if file is directory recover all child
        if (fileDTO.getIsDirectory()) {
            this.fileRepository.recoverChild(fileId);
        }
        // response file information
        return this.fileRepository.findFileDetailById(fileId.toString(), false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));

    }

    private FileDTO checkFileExist(UUID fileId) {
        FileDTO file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }
}
