package com.example.myvoca.dto;

import com.example.myvoca.entity.Stat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDto {
    private Integer wordId;
    private Integer correctCount;
    private Integer incorrectCount;
    private Integer isLearned;

    public static StatDto fromEntity(Stat stat) {
        return StatDto.builder()
                .wordId(stat.getWord().getWordId())
                .correctCount(stat.getCorrectCount())
                .incorrectCount(stat.getIncorrectCount())
                .isLearned(stat.getIsLearned())
                .build();
    }
}
