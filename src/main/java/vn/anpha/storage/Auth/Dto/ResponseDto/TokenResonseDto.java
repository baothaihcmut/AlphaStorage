package vn.anpha.storage.Auth.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;

@Getter
@Setter
@ToString

public class TokenResonseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResonseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
