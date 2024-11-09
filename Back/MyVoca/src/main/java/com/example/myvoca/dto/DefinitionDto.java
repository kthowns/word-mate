package com.example.myvoca.dto;

import com.example.myvoca.entity.Definition;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefinitionDto {
    private Integer definitionId;
    private Integer wordId;
    private String definition;
    private String type;
    public static DefinitionDto fromEntity(Definition definition){
        return DefinitionDto.builder()
                .definition(definition.getDefinition())
                .wordId(definition.getWord().getWordId())
                .type(definition.getType().getDescription())
                .definitionId(definition.getDefinitionId())
                .build();
    }
}
