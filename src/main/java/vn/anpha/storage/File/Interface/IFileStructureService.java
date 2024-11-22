package vn.anpha.storage.File.Interface;

import java.util.UUID;

import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;

public interface IFileStructureService {

    public FileDetailDTO updateFileInfo(UUID fileId, FileUpdateInfoDTO fileUpdateInfoRequest);

    public void deleteFileSoft(UUID fileId);

    public void moveFile(UUID fileId, MoveFileDTO moveFileRequest);

    public FileDetailDTO recoverFile(UUID fileId, RecoverFileDTO recoverFileRequest);
}
