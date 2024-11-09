package com.example.myvoca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VocabErrorCode {
    NO_USER("No user found"),
    NO_VOCAB("No vocab found"),
    NO_WORD("No word found"),
    NO_DEFINITION("No definition found"),
    NO_THEME("No theme found"),
    NO_STATS("No stats found"),
    BAD_REQUEST("Method is not supported"),
    INTERNAL_SERVER_ERROR("Internal server error");

    private final String errorMessage;
}
