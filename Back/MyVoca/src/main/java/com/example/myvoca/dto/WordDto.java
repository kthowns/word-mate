package com.example.myvoca.dto;

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
    private String expression;
    private Timestamp createdAt;
}
