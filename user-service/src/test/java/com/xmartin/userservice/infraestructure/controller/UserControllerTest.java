package com.xmartin.userservice.infraestructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.userservice.application.services.UserService;
import com.xmartin.userservice.domain.exceptions.EmailAlreadyInUseException;
import com.xmartin.userservice.domain.exceptions.UserNotFoundException;
import com.xmartin.userservice.domain.model.UserModel;
import com.xmartin.userservice.infraestructure.controller.dtos.UserRequest;
import com.xmartin.userservice.infraestructure.controller.dtos.UserResponse;
import com.xmartin.userservice.infraestructure.controller.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();

        userRequest = UserRequest.builder()
                .id(1)
                .email("xavi@test.com")
                .name("Xavi")
                .role("ROLE_USER")
                .build();

        userResponse = UserResponse.builder()
                .id(1)
                .email("xavi@test.com")
                .name("Xavi")
                .role("ROLE_USER")
                .build();
    }

    @Test
    void saveUser_success() throws Exception {
        //Given
        UserModel userModel = new UserModel();
        UserModel savedUserModel = new UserModel();
        when(userMapper.toModel(userRequest)).thenReturn(userModel);
        when(userService.saveUser(userModel)).thenReturn(savedUserModel);
        when(userMapper.toResponse(savedUserModel)).thenReturn(userResponse);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("xavi@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

    }

    @Test
    void saveUser_EmailAlreadyInUseException() throws Exception {
        //Given
        UserModel userModel = new UserModel();
        when(userMapper.toModel(userRequest)).thenReturn(userModel);
        when(userService.saveUser(userModel)).thenThrow(new EmailAlreadyInUseException(userModel.getEmail()));


        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));

    }

    @Test
    void updateUser_success() throws Exception {
        //Given
        Integer userId = 1;
        UserModel userModel = new UserModel();
        UserModel savedUserModel = new UserModel();
        when(userMapper.toModel(userRequest)).thenReturn(userModel);
        when(userService.updateUser(userModel, userId)).thenReturn(savedUserModel);
        when(userMapper.toResponse(savedUserModel)).thenReturn(userResponse);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("xavi@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));

    }

    @Test
    void updateUser_UserNotFoundException() throws Exception {
        //Given
        Integer userId = 1;
        UserModel userModel = new UserModel();
        when(userMapper.toModel(userRequest)).thenReturn(userModel);
        when(userService.updateUser(userModel, userId)).thenThrow(new UserNotFoundException(userModel.getEmail()));

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));


    }

    @Test
    void deleteUser_success() throws Exception {
        //Given
        String email = "xavi@test.com";

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/email/{email}}", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void deleteUser_UserNotFoundException() throws Exception {
        //Given
        String email = "xavi@test.com";
        doThrow(new UserNotFoundException(email)).when(userService).deleteUser(email);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/email/{email}", email))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void getUserByEmail_success() throws Exception {

        //Given
        String email = "xavi@test.com";
        UserModel savedUserModel = new UserModel();
        when(userService.getUserByEmail(email)).thenReturn(savedUserModel);
        when(userMapper.toResponse(savedUserModel)).thenReturn(userResponse);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("xavi@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void getUserByEmail_UserNotFoundException() throws Exception {

        //Given
        String email = "xavi@test.com";
        when(userService.getUserByEmail(email)).thenThrow(new UserNotFoundException(email));

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", email))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void getUserById_success() throws Exception {
        //Given
        Integer id = 1;
        UserModel savedUserModel = new UserModel();
        when(userService.getUserById(id)).thenReturn(savedUserModel);
        when(userMapper.toResponse(savedUserModel)).thenReturn(userResponse);

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("xavi@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void getUserById_UserNotFoundException() throws Exception {
        //Given
        Integer id = 1;
        when(userService.getUserById(id)).thenThrow(new UserNotFoundException(id));

        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void getUserExistByEmail_exists() throws Exception {
        //Given
        String email = "xavi@test.com";
        when(userService.userExists(email)).thenReturn(true);


        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/exist/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getUserExistByEmail_notExist() throws Exception {
        //Given
        String email = "xavi@test.com";
        when(userService.userExists(email)).thenReturn(false);


        //When - Then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/exist/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}