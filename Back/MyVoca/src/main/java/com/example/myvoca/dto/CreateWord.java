package com.example.myvoca.dto;

import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class CreateWord {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotNull
        private Integer vocabId;
        @NotNull
        @Size(max=32)
        private String expression;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer wordId;
        private String expression;
        private Timestamp createdAt;
        public static CreateWord.Response fromEntity(Word word){
            return Response.builder()
                    .wordId(word.getWordId())
                    .expression(word.getExpression())
                    .createdAt(word.getCreatedAt())
                    .build();
        }
    }
}
