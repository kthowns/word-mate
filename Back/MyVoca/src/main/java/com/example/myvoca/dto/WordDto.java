package com.example.myvoca.dto;

import com.example.myvoca.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordDto {
    private Integer wordId;
    private Integer vocabId;
    private String expression;
    private Timestamp createdAt;

    public static WordDto fromEntity(Word word) {
        return WordDto.builder()
                .wordId(word.getWordId())
                .vocabId(word.getVocab().getVocabId())
                .expression(word.getExpression())
                .createdAt(word.getCreatedAt())
                .build();
    }
}
