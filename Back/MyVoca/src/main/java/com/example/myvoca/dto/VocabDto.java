package com.example.myvoca.dto;

import com.example.myvoca.entity.Vocab;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VocabDto {
    private Integer vocabId;
    private Integer userId;
    private String title;
    private String description;
    private Integer wordCount;
    private Timestamp createdAt;

    public static VocabDto fromEntity(Vocab vocab) {
        return VocabDto.builder()
                .vocabId(vocab.getVocabId())
                .userId(vocab.getUser().getUserId())
                .title(vocab.getTitle())
                .description(vocab.getDescription())
                .wordCount(vocab.getWordCount())
                .createdAt(vocab.getCreatedAt())
                .build();
    }
}