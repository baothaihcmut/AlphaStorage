package vn.anpha.storage.Folder.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.Folder.DTO.Request.FolderCreationRequest;
import vn.anpha.storage.Folder.DTO.Response.FolderReponse;
import vn.anpha.storage.Folder.Entity.Folder;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    Folder toFolder(FolderCreationRequest dto);

    FolderReponse toFolderResponse(Folder folder);
}
