package vn.anpha.storage.File.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.TreeFileDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileStructureService implements IFileStructureService {
    private final FileRepository fileRepository;

    private void checkFileNameInDirectory(String fileParentId, String name) {
        List<FileDTO> subFiles = this.fileRepository.findAllFileInDirectory(false, fileParentId);
        boolean fileNameExist = subFiles.stream().anyMatch((file) -> file.getName().equals(name));
        if (fileNameExist) {
            throw new AppException(ErrorCode.FILE_NAME_EXIST_NAME);
        }
    }

    @Transactional
    public FileDTO updateFileDetail(String fileId, FileUpdateInfoDTO fileUpdateInfoRequest) {
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
        return this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public FileDTO deleteFileSoft(String fileId) {
        // get file in db
        FileDTO fileDTO = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        // set isDeleted to true
        this.fileRepository.softDeleteFile(fileId);

        // if file have field set child deleted
        if (fileDTO.getIsDirectory()) {
            this.fileRepository.softDeleteChild(fileId);
        }
        return this.fileRepository.findFileById(fileId, true)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public FileDTO moveFile(String fileId, MoveFileDTO moveFileRequest) {
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
        return this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
    }

    @Transactional
    public FileDTO recoverFile(String fileId, RecoverFileDTO recoverFileRequest) {
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
        return this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));

    }

    private FileDTO checkFileExist(String fileId) {
        FileDTO file = this.fileRepository.findFileById(fileId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_FILE_NOT_EXIST));
        return file;
    }

    public List<TreeFileDTO> getFileStructure(String departmentId) {
        List<FileDTO> files = this.fileRepository.findFileByDepartmentId(departmentId, false);
        List<TreeFileDTO> treeFiles = new ArrayList<>();
        Map<String, TreeFileDTO> fileMap = new HashMap<>();
        for (FileDTO file : files) {
            TreeFileDTO treeFile = new TreeFileDTO(file);
            fileMap.put(file.getFileId(), treeFile);
        }
        for (FileDTO file : files) {
            TreeFileDTO treeFile = fileMap.get(file.getFileId());
            if (file.getParentFileId() != null) {
                TreeFileDTO parent = fileMap.get(file.getParentFileId());
                if (parent != null) {
                    parent.addSubFile(treeFile);
                }
            } else {
                treeFiles.add(treeFile);
            }
        }
        return treeFiles;
    }

}
