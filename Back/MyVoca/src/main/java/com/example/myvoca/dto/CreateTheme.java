package com.example.myvoca.dto;

import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.Vocab;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class CreateTheme {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotNull
        private String font;
        @NotNull
        private Integer fontSize;
        @NotNull
        @Size(min=7,max=7)
        private String color;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private String font;
        private Integer fontSize;
        private String color;
        public static CreateTheme.Response fromEntity(Theme theme){
            return Response.builder()
                    .color(theme.getColor())
                    .font(theme.getFont())
                    .fontSize(theme.getFontSize())
                    .build();
        }
    }
}
