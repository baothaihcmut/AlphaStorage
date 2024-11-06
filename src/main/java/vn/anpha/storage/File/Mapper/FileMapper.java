package vn.anpha.storage.File.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;

@Mapper(componentModel = "spring")
public interface FileMapper {
    File toFile(FileCreationRequest dto);

    FileResponse toFileResponse(File file);

}
