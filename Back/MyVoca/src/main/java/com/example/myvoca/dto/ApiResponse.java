package com.example.myvoca.dto;

import com.example.myvoca.code.ApiResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T data;

    public static <T> ResponseEntity<?> toResponseEntity(ApiResponseCode responseCode, T data){
        return ResponseEntity.status(responseCode.getStatus())
                .body(ApiResponse.fromData(
                        responseCode, data));
    }

    private static <T> ApiResponse<?> fromData(ApiResponseCode responseCode, T data){
        return ApiResponse.builder()
                .data(data)
                .message(responseCode.getMessage())
                .status(responseCode.getStatus().value())
                .build();
    }
}
