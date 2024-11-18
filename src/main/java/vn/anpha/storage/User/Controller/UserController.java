package vn.anpha.storage.User.Controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Dto.RequestDto.ChangePasswordDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.User.mapper.UserResponseMapper;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.User.respository.UserRepositoryDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final UserResponseMapper userResponseMapper;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserResponseMapper userResponseMapper,
            UserRepository userRepository) {
        this.userService = userService;
        this.userResponseMapper = userResponseMapper;
        this.userRepository = userRepository;
    }

    @GetMapping("user/myInfo")
    public ApiResponseDto<User> getMyInfo() {
        ApiResponseDto<User> response = new ApiResponseDto<>();
        response.setResult(this.userService.GetInfo());
        return response;

    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/get/{id}")
    public ApiResponseDto<UserResponseDto> getUserById(@PathVariable() UUID id) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(userResponseMapper.User_To_UserResponseDto(this.userService.getUsersById(id)));
        return response;
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/getAll")
    public ApiResponseDto<PaginateResponseDto> getAllUser(@RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        int current = currentOptional.map(Integer::parseInt).orElse(1);
        int pageSize = pageSizeOptional.map(Integer::parseInt).orElse(10);
        Pageable pageable = PageRequest.of(current - 1, pageSize);

        ApiResponseDto<PaginateResponseDto> response = new ApiResponseDto<>();
        response.setResult(userService.GetAllUser(pageable));
        return response;
    }

    @PostMapping("/user/signUp")
    public ApiResponseDto<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto userDto) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.CreateUser(userDto));

        return response;
    }

    @PatchMapping("/user/update")
    public ApiResponseDto<UserResponseDto> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>();
        response.setResult(this.userService.UpdateUser(updateUserDto));
        return response;
    }

    @PatchMapping("/user/changePassword")
    public ApiResponseDto ChangePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
        ApiResponseDto response = new ApiResponseDto<>();
        this.userService.ChangePassword(changePasswordDto);
        return response;
    }

    @GetMapping("test1/{email}")
    public UserRepositoryDto test1(@PathVariable() String email) {
        return this.userService.test1(email);
    }

}
