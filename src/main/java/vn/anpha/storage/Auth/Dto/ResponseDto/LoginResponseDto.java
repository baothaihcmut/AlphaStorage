package vn.anpha.storage.Auth.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;

@Getter
@Setter
@ToString
public class LoginResponseDto {
    private UserResponseDto User;
    private TokenResonseDto token;

    public LoginResponseDto(vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto userResponseDto,
            TokenResonseDto token) {
        User = userResponseDto;
        this.token = token;
    }

}
