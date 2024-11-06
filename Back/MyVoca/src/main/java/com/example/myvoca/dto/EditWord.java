package com.example.myvoca.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EditWord {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotNull
        @Size(max=32)
        private String expression;
    }
}