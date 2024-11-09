package vn.anpha.storage.Auth.Controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import vn.anpha.storage.Auth.Dto.RequestDto.AuthoticationDto;
import vn.anpha.storage.Auth.Dto.RequestDto.RefreshTokenDto;
import vn.anpha.storage.Auth.Dto.ResponseDto.LoginResponseDto;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping()
public class AuthoticationController {
    UserRepository userRepository;
    AuthoticationService authenticationService;

    public AuthoticationController(UserRepository userRepository, AuthoticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/login")
    public ApiResponseDto<LoginResponseDto> isAuthenticated(@RequestBody @Valid AuthoticationDto authoticationDto) {
        ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>();
        LoginResponseDto loginResponseDto = authenticationService.Login(authoticationDto);

        response.setResult(loginResponseDto);

        return response;
    }

    @GetMapping("/auth/logout")
    public ApiResponseDto Logout() {
        ApiResponseDto response = new ApiResponseDto<>();
        authenticationService.Logout();
        return response;
    }

    @PostMapping("/auth/refresh")
    public ApiResponseDto<String> RefreshToken(@RequestBody @Valid RefreshTokenDto refreshToken) {
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
