package com.example.myvoca.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthResponseCode {
    INVALID_USERNAME(HttpStatus.NOT_FOUND, "Invalid username"),
    INVALID_PASSWORD(HttpStatus.CONFLICT, "Invalid password"),
    LOGIN_OK(HttpStatus.OK, "Login success");

    private final HttpStatus status;
    private final String responseMessage;
}
