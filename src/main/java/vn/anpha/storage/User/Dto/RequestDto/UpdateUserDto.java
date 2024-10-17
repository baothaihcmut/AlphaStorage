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
public class UpdateUserDto {

    @NotEmpty(message = "FullName cannot be empty")
    @Size(min = 3, max = 50, message = "fullName minLength=3 and maxLength=50")
    private String fullName;

    private String address;
    private String phone;
}
