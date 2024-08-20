package com.xmartin.authservice.infraestructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmartin.authservice.application.service.PasswordTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PasswordTokenController.class)
class PasswordTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordTokenService passwordTokenService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void forgotPassword_success() throws Exception {
        //Given
        String email = "xavi@example.com";

        //When - Then
        mockMvc.perform(post("/password-token/forgot-password")
                        .queryParam("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));

    }

    @Test
    void resetPassword_success() throws Exception {
        //Given
        String token = "token";
        String password = "123456";

        //When - Then
        mockMvc.perform(post("/password-token/reset-password")
                        .queryParam("token", token)
                        .queryParam("newPassword", password))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));

    }

}