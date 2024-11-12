package com.example.myvoca.dto;

import com.example.myvoca.entity.WordStats;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateStats {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @Min(0)
        private Integer correctCount;
        @NotBlank
        @Min(0)
        private Integer incorrectCount;
        @NotBlank
        @Min(0)
        @Max(1)
        private Integer isLearned;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer wordId;
        private Integer correctCount;
        private Integer incorrectCount;
        private Integer isLearned;
        public static UpdateStats.Response fromEntity(WordStats wordStats){
            return Response.builder()
                    .wordId(wordStats.getWordId())
                    .correctCount(wordStats.getCorrectCount())
                    .incorrectCount(wordStats.getIncorrectCount())
                    .isLearned(wordStats.getIsLearned())
                    .build();
        }
    }
}
