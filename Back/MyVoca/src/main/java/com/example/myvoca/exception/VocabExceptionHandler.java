package com.example.myvoca.exception;

import com.example.myvoca.dto.VocabErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import static com.example.myvoca.exception.VocabErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class VocabExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(VocabException.class)
    public VocabErrorResponse handleCustomException(
            VocabException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e.getVocabErrorCode(), request.getRequestURI(), e.getErrorMessage());
        return VocabErrorResponse.builder()
                .errorMessage(e.getErrorMessage())
                .vocabErrorCode(e.getVocabErrorCode())
                .build();
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class,
            BadRequestException.class})
    public VocabErrorResponse handleBadRequestException(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return VocabErrorResponse.builder()
                .errorMessage(VocabErrorCode.BAD_REQUEST.getErrorMessage())
                .vocabErrorCode(VocabErrorCode.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public VocabErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return VocabErrorResponse.builder()
                .vocabErrorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage())
                .build();
    }
}
