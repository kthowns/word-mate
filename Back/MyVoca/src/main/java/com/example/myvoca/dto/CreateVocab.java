package com.example.myvoca.dto;

import com.example.myvoca.entity.Vocab;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class CreateVocab {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @Size(max=16)
        private String title;
        @Size(max=32)
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer vocabId;
        private String title;
        private String description;
        private Timestamp createdAt;
        public static CreateVocab.Response fromEntity(Vocab vocab){
            String desc = vocab.getDescription();
            if(desc.length() > 15)
                desc = desc.substring(0,  15) + "...";
            return Response.builder()
                    .vocabId(vocab.getVocabId())
                    .title(vocab.getTitle())
                    .description(desc)
                    .createdAt(vocab.getCreatedAt())
                    .build();
        }
    }
}
