package vn.anpha.storage.File.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.File.DTO.Response.FileDetailResponse;
import vn.anpha.storage.File.Entity.FileDetail;

@Mapper(componentModel = "spring")
public interface FileDetailMapper {
    FileDetailResponse toFileDetailResponse(FileDetail model);
}
