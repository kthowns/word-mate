package com.example.myvoca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class VocabException extends RuntimeException {
    private VocabErrorCode vocabErrorCode;
    private String errorMessage;

    public VocabException(VocabErrorCode errorCode){
        super(errorCode.getErrorMessage());
        this.vocabErrorCode = errorCode;
        this.errorMessage = errorCode.getErrorMessage();
    }
}
