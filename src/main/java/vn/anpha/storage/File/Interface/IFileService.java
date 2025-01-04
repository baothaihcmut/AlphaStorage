package vn.anpha.storage.File.Interface;

import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Projection.StorageDetailDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileContentDTO;
import vn.anpha.storage.File.DTO.Response.CreateFileResponse;
import vn.anpha.storage.File.DTO.Response.UpdateFileContentResponse;

public interface IFileService {
    CreateFileResponse createFile(FileCreationDTO fileCreationDTO) throws Exception;

    StorageDetailDTO downloadFile(String id) throws Exception;

    FileMetaDataDTO announceUploadFile(String fileId, AnnounceUploadDTO annouceUploadDTO);

    UpdateFileContentResponse updateFileContent(String fileId, UpdateFileContentDTO dto) throws Exception;

    FileDTO getFileDetail(String fileId);
}
