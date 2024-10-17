package vn.anpha.storage.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Service.UserService;
import vn.anpha.storage.User.respository.UserRepository;

@Slf4j
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private CreateUserDto createUserDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void InitDate() {
        createUserDto = CreateUserDto.builder()
                .email("test@email.com")
                .password("test123")
                .fullName("Test")
                .address("Thu duc")
                .phone("123456")
                .build();

        userResponseDto = UserResponseDto.builder()
                .email("test@email.com")
                // .password("test123")
                .fullName("Test")
                .address("Thu duc")
                .phone("123456")
                .build();
    }
}
