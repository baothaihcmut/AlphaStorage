package vn.anpha.storage.Auth.mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    UserResponseDto User_To_User_Login(User user);
}
