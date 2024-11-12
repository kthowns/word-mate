package com.example.myvoca.service;

import com.example.myvoca.dto.LoginDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    public ResponseEntity<LoginDto.Response> login(LoginDto.Request request) {
        User user;
        try{
            user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(NoSuchElementException::new);
        }catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if(!user.getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(LoginDto.Response.fromEntity(user));
    }
}