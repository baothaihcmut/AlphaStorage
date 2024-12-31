package vn.anpha.storage.File.Interface;

import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataLinkDTO;

public interface IFileService {
    FileDetailUploadLinkDTO uploadFile(FileCreationDTO fileCreationDTO) throws Exception;

    FileMetaDataLinkDTO downloadFile(String id) throws Exception;

    FileMetaDataDTO announceUploadFile(String fileId, AnnounceUploadDTO annouceUploadDTO);

    FileMetaDataLinkDTO updateFile(String fileId, UpdateFileDTO dto) throws Exception;
}
