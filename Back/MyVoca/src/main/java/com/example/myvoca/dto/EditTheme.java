package com.example.myvoca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EditTheme {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        private String font;
        @NotBlank
        private Integer fontSize;
        @NotBlank
        @Size(min=7, max=7)
        private String color;
    }
}