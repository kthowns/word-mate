package com.example.myvoca.dto;

import com.example.myvoca.entity.Stat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpdateStat {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @Min(0)
        private Integer correctCount;
        @Min(0)
        private Integer incorrectCount;
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
        public static UpdateStat.Response fromEntity(Stat stat){
            return Response.builder()
                    .wordId(stat.getWord().getWordId())
                    .correctCount(stat.getCorrectCount())
                    .incorrectCount(stat.getIncorrectCount())
                    .isLearned(stat.getIsLearned())
                    .build();
        }
    }
}
