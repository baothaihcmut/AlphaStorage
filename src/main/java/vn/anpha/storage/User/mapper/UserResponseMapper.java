package vn.anpha.storage.User.mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseDto User_To_UserResponseDto(User user);
}
