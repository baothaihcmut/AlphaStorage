package vn.anpha.storage.User.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Dto.RequestDto.AuthoticationDto;
import vn.anpha.storage.Role.Service.RoleService;
import vn.anpha.storage.User.Dto.Projection.UserDto;
import vn.anpha.storage.User.Dto.RequestDto.ChangePasswordDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(RoleService roleService, UserRepository userRepository,
            PasswordEncoder passwordEncoder, UserMapper userMapper, UserResponseMapper userResponseMapper) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userResponseMapper = userResponseMapper;
    }

    public String hashPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    @Transactional
    public UserDto CreateUser(CreateUserDto userDto) {
        userDto.setPassword(hashPassword(userDto.getPassword()));
        String userId = UUID.randomUUID().toString();

        if (this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        try {
            long roleId = this.roleService.FindByName("USER").getRoleId();
            this.userRepository.createUser(userId, userDto, roleId);

            return userRepository.FindUserById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }

    }

    public PaginateResponseDto<UserResponseDto> GetAllUser(Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(pageable); // tư set limit offset
        var users = pageUser.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageUser.getNumber())
                .PageSize(pageUser.getSize())
                .TotalItems(pageUser.getTotalElements())
                .TotalPages(pageUser.getTotalPages())
                .build();
        PaginateResponseDto<UserResponseDto> responseDto = new PaginateResponseDto<UserResponseDto>();
        responseDto.setData(users.stream().map(userResponseMapper::User_To_UserResponseDto).toList());
        responseDto.setMetaPaginate(pageMeta);

        return responseDto;

    }

    public UserDto getUsersById(String id) {
        Optional<UserDto> user = userRepository.FindUserById(id);
        return user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public UserDto GetInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Optional<UserDto> user = userRepository.FindUserInfoByEmail(email);
        return user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public User GetUserByEmail(String email) {

        List<User> users = this.userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        } else {
            return users.get(0);
        }
    }

    public String GetUserIdByEmail(String email) {

        String userId = this.userRepository.findUserIdByEmail(email);
        if (userId == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return userId;
    }

    @Transactional
    public UserDto UpdateUser(UpdateUserDto updateUserDto) {
        SecurityContext context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        this.userRepository.updateUserByEmail(email, updateUserDto);
        Optional<UserDto> user = userRepository.FindUserInfoByEmail(email);
        return user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public boolean checkPassword(User user, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean isExactly = passwordEncoder.matches(password, user.getPassword());
        return isExactly;
    }

    public void ChangePassword(ChangePasswordDto changePasswordDto) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = this.GetUserByEmail(name);

        if (checkPassword(user, changePasswordDto.getOldPassword())) {
            user.setPassword(this.hashPassword(changePasswordDto.getNewPassword()));
            this.userRepository.save(user);

        } else {
            throw new AppException(ErrorCode.PASSWORD_NOT_EXACTLY);
        }

    }

}
