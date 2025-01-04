package vn.anpha.storage.Version.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.Repository.FileDetailRepository;
import vn.anpha.storage.Storage.DTO.VersionLinkDTO;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.Version.DTO.request.VersionCreationDTO;
import vn.anpha.storage.Version.Repository.VersionRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class VersionService {
        private final StorageService storageService;
        private final VersionRepository versionRepository;
        private final FileDetailRepository fileDetailRepository;
        private final AuthoticationService authService;

        public void createVersion(String fileId, String description) {
                FileMetaDataDTO fileMetaDataDTO = this.fileDetailRepository.findFileMetaDataById(fileId)
                                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                User user = this.authService.getUserByToken();
                // get version id, size, update time from storage
                VersionLinkDTO versionLinkDTO = this.storageService.getLastVersion(fileMetaDataDTO.getBucketName(),
                                fileMetaDataDTO.getLink());
                // create new version create dto
                VersionCreationDTO versionCreationDTO = new VersionCreationDTO(
                                UUID.randomUUID().toString(), versionLinkDTO.getLink(), description,
                                versionLinkDTO.getSize(),
                                fileMetaDataDTO.getFileId(), user.getUserId(), versionLinkDTO.getCreateAt());
                // persistence to db
                this.versionRepository.insertVersion(versionCreationDTO);
                // response version

        }

}
