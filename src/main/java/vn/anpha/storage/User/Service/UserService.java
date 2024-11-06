package vn.anpha.storage.User.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Detail.Service.DetailService;
import vn.anpha.storage.Role.Service.RoleService;
import vn.anpha.storage.User.Dto.RequestDto.ChangePasswordDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserPaginateResponseDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.mapper.UserMapper;
import vn.anpha.storage.User.mapper.UserResponseMapper;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Slf4j
@Service

public class UserService {
    private final RoleService roleService;
    private final DetailService detailService;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(RoleService roleService, DetailService detailService, UserRepository userRepository,
            PasswordEncoder passwordEncoder, UserMapper userMapper, UserResponseMapper userResponseMapper) {
        this.roleService = roleService;
        this.detailService = detailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userResponseMapper = userResponseMapper;
    }

    public String hashPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public UserResponseDto CreateUser(CreateUserDto userDto) {
        userDto.setPassword(hashPassword(userDto.getPassword()));
        User user = userMapper.createToUser(userDto);
        // user.setRole();
        user.setRole(this.roleService.FindByName("USER"));
        try {

            user = this.userRepository.save(user);

            this.detailService.createDetail(user);

            UserResponseDto userResponseDto = userResponseMapper.User_To_UserResponseDto(user);

            return userResponseDto;
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

    }

    public UserPaginateResponseDto GetAllUser(Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(pageable);
        var users = pageUser.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageUser.getNumber())
                .PageSize(pageUser.getSize())
                .TotalItems(pageUser.getTotalElements())
                .TotalPages(pageUser.getTotalPages())
                .build();
        UserPaginateResponseDto responseDto = UserPaginateResponseDto.builder()
                .data(users.stream().map(userResponseMapper::User_To_UserResponseDto).toList())
                .metaPaginate(pageMeta)
                .build();

        return responseDto;

    }

    public User getUsersById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user;
    }

    public UserResponseDto GetInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        return userResponseMapper.User_To_UserResponseDto(this.GetUserByEmail(name));
    }

    public User GetUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).get(0);
        return user;
    }

    public UserResponseDto UpdateUser(UpdateUserDto update) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = this.GetUserByEmail(name);
        userMapper.userUpdate(user, update);
        this.userRepository.save(user);
        return userResponseMapper.User_To_UserResponseDto(user);
    }

    public void ChangePassword(ChangePasswordDto changePasswordDto) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = this.GetUserByEmail(name);
        String hassPassWord = this.hashPassword(changePasswordDto.getOldPassword());
        if (this.passwordEncoder.matches(user.getPassword(), hassPassWord)) {
            user.setPassword(this.hashPassword(changePasswordDto.getNewPassword()));
            this.userRepository.save(user);

        } else {
            throw new AppException(ErrorCode.PASSWORD_NOT_EXACTLY);
        }

    }
}
