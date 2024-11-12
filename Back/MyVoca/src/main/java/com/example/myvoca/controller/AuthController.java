package com.example.myvoca.controller;

import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.LoginDto;
import com.example.myvoca.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.myvoca.code.ApiResponseCode.LOGIN_OK;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    @PostMapping("/api/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginDto.Request request
    ){
        log.info("HTTP POST /api/def/" + request);

        return ApiResponse.toResponseEntity(LOGIN_OK, authService.login(request));
    }
}
