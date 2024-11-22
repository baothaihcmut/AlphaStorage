package vn.anpha.storage.File.Interface;

import java.util.UUID;

import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;

public interface IFileStructureService {
    public FileDetailUploadLinkDTO createFile(FileCreationDTO dto) throws Exception;

    public FileDetailDTO updateFileInfo(UUID fileId, FileUpdateInfoDTO fileUpdateInfoRequest);

    public void deleteFileSoft(UUID fileId);

    public void moveFile(UUID fileId, MoveFileDTO moveFileRequest);

    public FileDetailDTO recoverFile(UUID fileId, RecoverFileDTO recoverFileRequest);
}
