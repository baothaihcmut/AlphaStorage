package vn.anpha.storage.File.Interface;

import java.util.UUID;

import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoRequest;
import vn.anpha.storage.File.DTO.Request.MoveFileRequest;
import vn.anpha.storage.File.DTO.Request.RecoverFileRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;

public interface IFileStructureService {
    public FileResponse createFile(FileCreationRequest dto) throws Exception;

    public FileResponse updateFileInfo(UUID fileId, FileUpdateInfoRequest fileUpdateInfoRequest);

    public void deleteFileSoft(UUID fileId);

    public void moveFile(UUID fileId, MoveFileRequest moveFileRequest);

    public void recoverFile(UUID fileId, RecoverFileRequest recoverFileRequest);
}
