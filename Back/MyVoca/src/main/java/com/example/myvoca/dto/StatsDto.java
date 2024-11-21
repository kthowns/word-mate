package com.example.myvoca.dto;

import com.example.myvoca.entity.Stats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsDto {
    private Integer wordId;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer isLearned;

    public static StatsDto fromEntity(Stats stats) {
        return StatsDto.builder()
                .wordId(stats.getWord().getWordId())
                .correctCount(stats.getCorrectCount())
                .incorrectCount(stats.getIncorrectCount())
                .isLearned(stats.getIsLearned())
                .build();
    }
}
