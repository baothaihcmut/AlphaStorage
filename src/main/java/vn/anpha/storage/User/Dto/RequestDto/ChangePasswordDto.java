package vn.anpha.storage.User.Dto.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ChangePasswordDto {

    @NotEmpty(message = "oldPassword cannot be empty")
    @Size(min = 3, max = 150)
    private String oldPassword;

    @NotEmpty(message = "newPassword cannot be empty")
    @Size(min = 3, max = 150)
    private String newPassword;

}
