package vn.anpha.storage.User.Controller;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Dto.RequestDto.ChangePasswordDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserPaginateResponseDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.User.mapper.UserMapper;
import vn.anpha.storage.User.mapper.UserResponseMapper;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;

    public UserController(UserService userService, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping("user/myinfo")
    public ApiResponseDto<UserResponseDto> GetMyInfo() {

        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.GetInfo());
        return response;

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public ApiResponseDto<UserResponseDto> Getuserbyid(@RequestParam("id") UUID id) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(userResponseMapper.User_To_UserResponseDto(this.userService.getUsersById(id)));
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/getall")
    public ApiResponseDto<UserPaginateResponseDto> GetAllUser(@RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        int current = currentOptional.isPresent() ? Integer.parseInt(currentOptional.get()) : 1;
        int pageSize = currentOptional.isPresent() ? Integer.parseInt(currentOptional.get()) : 10;
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        ApiResponseDto<UserPaginateResponseDto> response = new ApiResponseDto<>();
        response.setResult(userService.GetAllUser(pageable));
        return response;
    }

    @PostMapping("/user/create")
    public ApiResponseDto<UserResponseDto> CreateUser(@RequestBody @Valid CreateUserDto userDto) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.CreateUser(userDto));
        return response;
    }

    @PostMapping("/user/update")
    public ApiResponseDto<UserResponseDto> UpdateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.UpdateUser(updateUserDto));
        return response;
    }

    @PostMapping("/user/changePassword")
    public ApiResponseDto ChangePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        ApiResponseDto response = new ApiResponseDto<>();
        this.userService.ChangePassword(changePasswordDto);
        return response;
    }
}
