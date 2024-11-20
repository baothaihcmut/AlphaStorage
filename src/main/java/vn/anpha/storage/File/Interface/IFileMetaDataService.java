package vn.anpha.storage.File.Interface;

import java.util.UUID;

public interface IFileMetaDataService {
    void updateUploadStatus(UUID fileId, Boolean status);

    void deleteHard(UUID fileId);
}
