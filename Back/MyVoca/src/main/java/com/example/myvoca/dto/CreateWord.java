package com.example.myvoca.dto;

import com.example.myvoca.entity.Word;
import jakarta.validation.constraints.NotBlank;
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
        @NotBlank
        @Size(max=32)
        private String expression;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer wordId;
        private Integer vocabId;
        private String expression;
        private Timestamp createdAt;
        public static CreateWord.Response fromEntity(Word word){
            return Response.builder()
                    .wordId(word.getWordId())
                    .vocabId(word.getVocab().getVocabId())
                    .expression(word.getExpression())
                    .createdAt(word.getCreatedAt())
                    .build();
        }
    }
}