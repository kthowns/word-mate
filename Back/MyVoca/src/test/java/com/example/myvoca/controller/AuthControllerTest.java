package com.example.myvoca.controller;

import com.example.myvoca.dto.LoginDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.myvoca.code.ApiResponseCode.LOGIN_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    User defaultUser = User.builder()
            .userId(1)
            .username("user01")
            .password("pass01")
            .build();

    @DisplayName("[API][POST] 로그인 성공")
    @Test
    void loginTest_success() throws Exception {
        LoginDto.Request request = toRequest(defaultUser);
        LoginDto.Response response = LoginDto.Response.builder()
                .userId(defaultUser.getUserId())
                .username(defaultUser.getUsername())
                .build();
        //given
        given(authService.login(request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data.userId",
                                is(defaultUser.getUserId()))
                ).andExpect(
                        jsonPath("$.data.username",
                                is(defaultUser.getUsername()))
                ).andExpect(
                        jsonPath("$.message",
                                is(LOGIN_OK.getMessage()))
                );
    }

    private LoginDto.Request toRequest(User user) {
        return LoginDto.Request.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}