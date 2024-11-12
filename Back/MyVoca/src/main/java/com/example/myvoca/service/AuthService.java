package com.example.myvoca.service;

import com.example.myvoca.code.ApiResponseCode;
import com.example.myvoca.dto.LoginDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.myvoca.code.ApiResponseCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    public LoginDto.Response login(LoginDto.Request request) {
        User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ApiException(NO_USER));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ApiException(INVALID_PASSWORD);
        }

        return LoginDto.Response.fromEntity(user);
    }
}