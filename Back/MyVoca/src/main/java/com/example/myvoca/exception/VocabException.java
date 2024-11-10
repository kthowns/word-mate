package com.example.myvoca.exception;

import lombok.Getter;

@Getter
public class VocabException extends RuntimeException {
    private VocabErrorCode vocabErrorCode;
    private Integer statusCode;
    private String errorMessage;

    public VocabException(VocabErrorCode errorCode){
        super(errorCode.getErrorMessage());
        this.vocabErrorCode = errorCode;
        this.statusCode = errorCode.getCode();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
