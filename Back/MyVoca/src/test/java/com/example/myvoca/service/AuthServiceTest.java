package com.example.myvoca.service;

import com.example.myvoca.dto.LoginDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.myvoca.code.ApiResponseCode.INVALID_PASSWORD;
import static com.example.myvoca.code.ApiResponseCode.NO_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private final User defaultUser = User.builder()
            .userId(1)
            .username("user")
            .password("pass")
            .build();

    @DisplayName("[API][POST] 로그인 성공")
    @Test
    void loginTest_success(){
        LoginDto.Request request = LoginDto.Request.builder()
                .username("user")
                .password("pass")
                .build();

        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(defaultUser));
        //when
        LoginDto.Response response = authService.login(request);

        //then
        assertEquals(response.getUsername(), request.getUsername());
    }

    @DisplayName("[API][POST] 로그인 Username 실패")
    @Test
    void loginTest_failed_username(){
        LoginDto.Request request = LoginDto.Request.builder()
                .username("user01")
                .password("pass01")
                .build();

        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());

        //when
        Throwable e = assertThrows(Exception.class, ()-> {
            authService.login(request);
        });

        //then
        assertEquals(e.getMessage(), NO_USER.getMessage());
    }

    @DisplayName("[API][POST] 로그인 Password 실패")
    @Test
    void loginTest_failed_password(){
        LoginDto.Request request = LoginDto.Request.builder()
                .username(defaultUser.getUsername())
                .password("pass01")
                .build();

        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(defaultUser));

        //when
        Throwable e = assertThrows(Exception.class, ()-> {
            authService.login(request);
        });

        //then
        assertEquals(e.getMessage(), INVALID_PASSWORD.getMessage());
    }
}