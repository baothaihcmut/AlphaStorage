package vn.anpha.storage.Auth.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;

@Getter
@Setter
@ToString
public class LoginResponseDto {
    private UserResponseDto UserResponseDto;
    private String token;
    private String refreshToken;

    public LoginResponseDto(UserResponseDto UserResponseDto, String token, String refreshToken) {
        this.UserResponseDto = UserResponseDto;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
