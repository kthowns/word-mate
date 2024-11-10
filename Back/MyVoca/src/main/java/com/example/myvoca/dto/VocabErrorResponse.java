package com.example.myvoca.dto;

import com.example.myvoca.exception.VocabErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabErrorResponse {
    private VocabErrorCode vocabErrorCode;
    private Integer statusCode;
    private String errorMessage;
}
