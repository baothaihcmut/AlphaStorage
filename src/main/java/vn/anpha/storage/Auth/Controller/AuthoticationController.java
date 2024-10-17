package vn.anpha.storage.Auth.Controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import vn.anpha.storage.Auth.Dto.RequestDto.AuthoticationDto;
import vn.anpha.storage.Auth.Dto.RequestDto.RefreshTokenDto;
import vn.anpha.storage.Auth.Dto.ResponseDto.LoginResponseDto;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
public class AuthoticationController {
    UserRepository userRepository;
    AuthoticationService authenticationService;

    public AuthoticationController(UserRepository userRepository, AuthoticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authent/login")
    public ApiResponseDto<LoginResponseDto> isAuthenticated(@RequestBody AuthoticationDto authoticationDto) {
        ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>();
        LoginResponseDto loginResponseDto = authenticationService.Login(authoticationDto);

        response.setResult(loginResponseDto);

        return response;
    }

    @PostMapping("/auth/RefreshToken")
    public ApiResponseDto<String> RefreshToken(@RequestBody RefreshTokenDto refreshToken) {
        ApiResponseDto<String> response = new ApiResponseDto<>();
        String token;
        try {
            token = this.authenticationService.instropectRefreshToken(refreshToken.getRefreshToken());
            response.setResult(token);

            return response;
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            throw new AppException(ErrorCode.RefreshToken_Not_Valid);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            throw new AppException(ErrorCode.RefreshToken_Not_Valid);
        }
    }
}
