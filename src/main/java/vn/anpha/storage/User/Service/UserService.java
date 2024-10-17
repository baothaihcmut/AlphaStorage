package vn.anpha.storage.User.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.anpha.storage.Auth.mapper.UserResponseMapper;
import vn.anpha.storage.Role.Service.RoleService;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.mapper.UserMapper;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
public class UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(
            RoleService roleService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            UserResponseMapper userResponseMapper) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userResponseMapper = userResponseMapper;
    }

    public String getUserName() {
        return "hello";
    }

    public String hashPassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public UserResponseDto CreateUser(CreateUserDto userDto) {
        userDto.setPassword(hashPassword(userDto.getPassword()));
        User user = userMapper.createToUser(userDto);
        // user.setRoleId();
        user.setRoleId(this.roleService.FindByName("User"));
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserResponseDto userResponseDto = userResponseMapper.User_To_UserResponseDto(user);
        return userResponseDto;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUsersById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user;
    }

    public User GetUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).get(0);

        return user;
    }

    public User deleteById(UUID id) {
        return userRepository.removeById(id);
    }

    // public User updateUserById(long id, User userdto) {
    // User currentUser = this.getUsersById(id);
    // if (currentUser != null) {

    // if (userdto.getFullName() != null) {
    // currentUser.setFullName(userdto.getFullName());
    // }
    // if (userdto.getPhone() != null) {
    // currentUser.setPhone(userdto.getPhone());
    // }
    // if (userdto.getAddress() != null) {
    // currentUser.setAddress(userdto.getAddress());
    // }

    // }
    // return this.CreateUser(currentUser);
    // }
}
