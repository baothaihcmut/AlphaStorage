package vn.anpha.storage.Auth.Dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
