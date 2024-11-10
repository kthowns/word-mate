package com.example.myvoca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VocabErrorCode {
    NO_USER(404, "No user found"),
    NO_VOCAB(404, "No vocab found"),
    NO_WORD(404, "No word found"),
    NO_DEFINITION(404, "No definition found"),
    NO_THEME(404, "No theme found"),
    NO_STATS(404, "No stats found"),
    BAD_REQUEST(400, "Method is not supported"),
    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private final Integer code;
    private final String errorMessage;
}
