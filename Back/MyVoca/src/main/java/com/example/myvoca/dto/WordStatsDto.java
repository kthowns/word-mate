package com.example.myvoca.dto;

import com.example.myvoca.entity.WordStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordStatsDto {
    private Integer wordId;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer isLearned;

    public static WordStatsDto fromEntity(WordStats wordStats) {
        return WordStatsDto.builder()
                .wordId(wordStats.getWordId())
                .correctCount(wordStats.getCorrectCount())
                .incorrectCount(wordStats.getIncorrectCount())
                .isLearned(wordStats.getIsLearned())
                .build();
    }
}
