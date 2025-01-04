package vn.anpha.storage.User.Controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Dto.Projection.UserDto;
import vn.anpha.storage.User.Dto.RequestDto.ChangePasswordDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("user/myInfo")
    public ApiResponseDto<UserDto> getMyInfo() {
        ApiResponseDto<UserDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.GetInfo());
        response.setMessage("Get MyInfo User");
        return response;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/get/{id}")
    public ApiResponseDto<UserDto> getUserById(@PathVariable() String id) {
        ApiResponseDto<UserDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.getUsersById(id));
        response.setMessage("Get MyInfo User By Id");
        return response;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/getAll")
    public ApiResponseDto<PaginateResponseDto<UserResponseDto>> getAllUser(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        int current = currentOptional.map(Integer::parseInt).orElse(1);
        int pageSize = pageSizeOptional.map(Integer::parseInt).orElse(10);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        ApiResponseDto<PaginateResponseDto<UserResponseDto>> response = new ApiResponseDto<>();
        response.setResult(userService.GetAllUser(pageable));
        response.setMessage("Get Info All Users");
        return response;
    }

    @PostMapping("/user/signUp")
    public ApiResponseDto<UserDto> createUser(@RequestBody @Valid CreateUserDto userDto) {
        ApiResponseDto<UserDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.CreateUser(userDto));

        return response;
    }

    @PatchMapping("/user/update")
    public ApiResponseDto<UserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        ApiResponseDto<UserDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.UpdateUser(updateUserDto));
        response.setMessage("Update Information User");

        return response;
    }

    @PatchMapping("/user/changePassword")
    public ApiResponseDto<?> ChangePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        ApiResponseDto<?> response = new ApiResponseDto<>();
        this.userService.ChangePassword(changePasswordDto);
        response.setMessage("User Change Password ");

        return response;
    }

}
