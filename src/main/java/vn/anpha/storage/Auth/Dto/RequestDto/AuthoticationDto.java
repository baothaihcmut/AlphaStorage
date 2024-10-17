package vn.anpha.storage.Auth.Dto.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthoticationDto {
    @Email(message = "Username is email")
    @NotEmpty(message = "Username is not empty")
    private String username;

    @NotEmpty(message = "Password is not empty")
    private String password;
}
