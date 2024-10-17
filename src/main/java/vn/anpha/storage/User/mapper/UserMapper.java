package vn.anpha.storage.User.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createToUser(CreateUserDto createUserDto);

    void userUpdate(@MappingTarget User user, UpdateUserDto updateUserDto);
}