package vn.anpha.storage.File.Interface;

import java.util.List;

import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.TreeFileDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.MoveFileDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;

public interface IFileStructureService {

    public FileDTO updateFileDetail(String fileId, FileUpdateInfoDTO fileUpdateInfoRequest);

    public FileDTO deleteFileSoft(String fileId);

    public FileDTO moveFile(String fileId, MoveFileDTO moveFileRequest);

    public FileDTO recoverFile(String fileId, RecoverFileDTO recoverFileRequest);

    public List<TreeFileDTO> getFileStructure(String departmentId);
}
