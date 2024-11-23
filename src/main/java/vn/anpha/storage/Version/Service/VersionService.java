package vn.anpha.storage.Version.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Response.FileMetaDataDTO;
import vn.anpha.storage.Storage.DTO.VersionLinkDTO;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.DTO.request.VersionCreationDTO;
import vn.anpha.storage.Version.DTO.response.VersionDTO;
import vn.anpha.storage.Version.Repository.VersionRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class VersionService {
        private final StorageService storageService;
        private final VersionRepository versionRepository;

        public VersionDTO createVersion(FileMetaDataDTO fileMetaDataDTO, String description, User user) {
                // get version id, size, update time from storage
                VersionLinkDTO versionLinkDTO = this.storageService.getLastVersion(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink());
                // create new version create dto
                VersionCreationDTO versionCreationDTO = new VersionCreationDTO(
                                UUID.randomUUID(), versionLinkDTO.getLink(), description, versionLinkDTO.getSize(),
                                fileMetaDataDTO.getFileId(), user.getUserId());
                // persistence to db
                this.versionRepository.insertVersion(versionCreationDTO);
                // response version
                return this.versionRepository.findVersionById(versionCreationDTO.getVersionId())
                                .orElseThrow(() -> new AppException(ErrorCode.VERSION_NOT_EXIST));
        }

}
