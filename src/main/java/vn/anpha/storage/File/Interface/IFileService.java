package vn.anpha.storage.File.Interface;

import java.util.UUID;

import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataLinkDTO;

public interface IFileService {
    FileDetailUploadLinkDTO uploadFile(FileCreationDTO fileCreationDTO) throws Exception;

    FileMetaDataLinkDTO downloadFile(UUID id) throws Exception;

    FileMetaDataDTO announceUploadFile(UUID fileId, AnnounceUploadDTO annouceUploadDTO);

    FileMetaDataLinkDTO updateFile(UUID fileId, UpdateFileDTO dto) throws Exception;
}
