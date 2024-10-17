package vn.anpha.storage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Service.UserService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

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

    @Test
    void CreateUser_validRequest_success() throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.registerModule(new JavaTimeModule());

        String content = objectmapper.writeValueAsString(createUserDto);

        Mockito.when(userService.CreateUser(ArgumentMatchers.any())).thenReturn(userResponseDto);

        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // .andExpect(MockMvcResultMatchers.jsonPath("key In json").value("value key"));

    }

    @Test
    void CreateUser_validRequest_fail() throws Exception {
        createUserDto.setFullName("12");
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.registerModule(new JavaTimeModule());

        String content = objectmapper.writeValueAsString(createUserDto);

        Mockito.when(userService.CreateUser(ArgumentMatchers.any())).thenReturn(userResponseDto);

        mvc.perform(MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("fullName minLength=3 and maxLength=50"));
        ;
    }
}
