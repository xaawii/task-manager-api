package com.xmartin.authservice.infraestructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.authservice.application.service.AuthService;
import com.xmartin.authservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.authservice.domain.exceptions.InvalidTokenException;
import com.xmartin.authservice.domain.exceptions.UserNotFoundException;
import com.xmartin.authservice.domain.exceptions.WrongPasswordException;
import com.xmartin.authservice.domain.model.LoginModel;
import com.xmartin.authservice.domain.model.RegisterModel;
import com.xmartin.authservice.domain.model.RequestModel;
import com.xmartin.authservice.domain.model.UserModel;
import com.xmartin.authservice.infraestructure.dto.LoginDto;
import com.xmartin.authservice.infraestructure.dto.RegisterDto;
import com.xmartin.authservice.infraestructure.dto.RequestDto;
import com.xmartin.authservice.infraestructure.mappers.LoginMapper;
import com.xmartin.authservice.infraestructure.mappers.RegisterMapper;
import com.xmartin.authservice.infraestructure.mappers.RequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthUserController.class)
class AuthUserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private LoginMapper loginMapper;

    @MockBean
    private RegisterMapper registerMapper;

    @MockBean
    private RequestMapper requestMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void login_success() throws Exception {

        //Given
        LoginDto loginDto = LoginDto.builder()
                .email("xavi@test.com")
                .password("password")
                .build();
        LoginModel loginModel = new LoginModel();

        when(loginMapper.loginDtoToModel(loginDto)).thenReturn(loginModel);
        when(authService.login(loginModel)).thenReturn("newToken");

        //When - Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("newToken"));
    }

    @Test
    void login_UserNotFoundException() throws Exception {

        //Given
        LoginDto loginDto = LoginDto.builder()
                .email("xavi@test.com")
                .password("password")
                .build();
        LoginModel loginModel = new LoginModel();

        when(loginMapper.loginDtoToModel(loginDto)).thenReturn(loginModel);
        when(authService.login(loginModel)).thenThrow(new UserNotFoundException());

        //When - Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void login_WrongPasswordException() throws Exception {

        //Given
        LoginDto loginDto = LoginDto.builder()
                .email("xavi@test.com")
                .password("password")
                .build();
        LoginModel loginModel = new LoginModel();

        when(loginMapper.loginDtoToModel(loginDto)).thenReturn(loginModel);
        when(authService.login(loginModel)).thenThrow(new WrongPasswordException());

        //When - Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void validate_success() throws Exception {

        //Given
        RequestDto requestDto = new RequestDto();
        RequestModel requestModel = new RequestModel();

        when(requestMapper.requestDtoToModel(requestDto)).thenReturn(requestModel);
        when(authService.validate("token", requestModel)).thenReturn("newToken");

        //When - Then
        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", "token")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("newToken"));
    }

    @Test
    void validate_InvalidTokenException() throws Exception {

        //Given
        RequestDto requestDto = new RequestDto();
        RequestModel requestModel = new RequestModel();

        when(requestMapper.requestDtoToModel(requestDto)).thenReturn(requestModel);
        when(authService.validate("token", requestModel)).thenThrow(new InvalidTokenException());

        //When - Then
        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", "token")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void save_success() throws Exception {
        //Given
        RegisterDto registerDto = RegisterDto.builder()
                .name("Xavi")
                .email("xavi@test.com")
                .password("password")
                .build();
        RegisterModel registerModel = new RegisterModel();
        UserModel userModel = UserModel.builder()
                .name("Xavi")
                .email("xavi@test.com")
                .build();

        when(registerMapper.registerDtoToModel(registerDto)).thenReturn(registerModel);
        when(authService.register(registerModel)).thenReturn(userModel);

        //When - Then
        mockMvc.perform(post("/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("xavi@test.com"));
    }

    @Test
    void save_EmailAlreadyInUseException() throws Exception {
        //Given
        RegisterDto registerDto = RegisterDto.builder()
                .name("Xavi")
                .email("xavi@test.com")
                .password("password")
                .build();
        RegisterModel registerModel = new RegisterModel();

        when(registerMapper.registerDtoToModel(registerDto)).thenReturn(registerModel);
        when(authService.register(registerModel)).thenThrow(new EmailAlreadyInUseException(registerDto.getEmail()));

        //When - Then
        mockMvc.perform(post("/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }
}