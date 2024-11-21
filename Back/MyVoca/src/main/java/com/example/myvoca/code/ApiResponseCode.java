package com.example.myvoca.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {
    OK(HttpStatus.OK, "OK"),
    INVALID_USERNAME(HttpStatus.NOT_FOUND, "Invalid username"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    LOGIN_OK(HttpStatus.OK, "Login success"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    INVALID_REQUEST(HttpStatus.NOT_FOUND, "Invalid Request"),
    NO_USER(HttpStatus.NOT_FOUND, "No user found"),
    NO_VOCAB(HttpStatus.NOT_FOUND, "No vocab found"),
    NO_WORD(HttpStatus.NOT_FOUND, "No word found"),
    NO_DEFINITION(HttpStatus.NOT_FOUND, "No definition found"),
    NO_THEME(HttpStatus.NOT_FOUND, "No theme found"),
    NO_STATS(HttpStatus.NOT_FOUND, "No stats found"),
    DUPLICATED_TITLE(HttpStatus.CONFLICT, "Title is duplicated"),
    DUPLICATED_WORD(HttpStatus.CONFLICT, "Word is duplicated"),
    DUPLICATED_DEFINITION(HttpStatus.CONFLICT, "Definition is duplicated"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Method is not supported"),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "Invalid Request Body"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;
}
