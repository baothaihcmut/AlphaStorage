package vn.anpha.storage.File.Mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.Tag.Entity.Tag;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(source = "fileId", target = "parentFile.fileId")
    @Mapping(source = "departmentId", target = "department.departmentId")
    @Mapping(expression = "java(mapTagIds(dto.getTagIds()))", target = "tags")
    File toFile(FileCreationRequest dto);

    FileResponse toFileResponse(File file);

    default List<Tag> mapTagIds(UUID[] tagIds) {
        return Stream.of(tagIds).map((tagId) -> {
            Tag tag = new Tag();
            tag.setTagId(tagId);
            return tag;
        }).toList();
    }

}
