package com.example.myvoca.integration;

import com.example.myvoca.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.myvoca.code.ApiResponseCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][POST] 로그인 Test")
    @Test
    void loginTest() throws Exception {
        //실제 DB에 존재하는 사용자
        LoginDto.Request request = LoginDto.Request.builder()
                .username("user1")
                .password("password1")
                .build();

        //로그인 성공 시
        mockMvc.perform(post("/api/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data.userId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.username")
                                .value(request.getUsername())
                ).andExpect(
                        jsonPath("$.message")
                                .value(LOGIN_OK.getMessage())
                );

        //올바르지 않은 비밀번호
        request.setPassword("wrongPass");
        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(
                        jsonPath("$.message")
                                .value(INVALID_PASSWORD.getMessage())
                );

        //없는 사용자
        request.setUsername("wrongUser");
        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value(NO_USER.getMessage())
                );
    }
}
