package vn.anpha.storage.File.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(source = "folderId", target = "folder.folderId")
    @Mapping(source = "departmentId", target = "department.departmentId")
    File toFile(FileCreationRequest dto);

    FileResponse toFileResponse(File file);

}
