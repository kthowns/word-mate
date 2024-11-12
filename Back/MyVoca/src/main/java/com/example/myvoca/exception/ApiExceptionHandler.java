package com.example.myvoca.exception;

import com.example.myvoca.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.example.myvoca.code.ApiResponseCode.*;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleCustomException(
            ApiException e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}, message : {}",
                e.getResponseCode(), request.getRequestURI(), e.getMessage());

        return ApiResponse.toResponseEntity(e.getResponseCode(), null);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,
            BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return ApiResponse.toResponseEntity(BAD_REQUEST, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNotFoundException(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return ApiResponse.toResponseEntity(NOT_FOUND, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            Exception e, HttpServletRequest request
    ){
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return ApiResponse.toResponseEntity(INVALID_REQUEST_BODY, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("errorCode : {}, uri : {}",
                e, request.getRequestURI());
        return ApiResponse.toResponseEntity(INTERNAL_SERVER_ERROR, null);
    }
}
