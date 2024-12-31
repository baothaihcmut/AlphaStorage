package vn.anpha.storage.File.Interface;

import vn.anpha.storage.File.DTO.Projection.FileDetailDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;

public interface IFileStructureService {

    public FileDetailDTO updateFileInfo(String fileId, FileUpdateInfoDTO fileUpdateInfoRequest);

    public void deleteFileSoft(String fileId);

    public void moveFile(String fileId, MoveFileDTO moveFileRequest);

    public FileDetailDTO recoverFile(String fileId, RecoverFileDTO recoverFileRequest);
}
