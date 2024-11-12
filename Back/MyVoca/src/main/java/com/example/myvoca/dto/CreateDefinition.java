package com.example.myvoca.dto;

import com.example.myvoca.entity.Definition;
import com.example.myvoca.type.POS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateDefinition {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @Size(max=64)
        private String definition;
        @NotBlank
        private POS type;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer definitionId;
        private Integer wordId;
        private String definition;
        private String type;
        public static CreateDefinition.Response fromEntity(Definition definition){
            return Response.builder()
                    .definitionId(definition.getDefinitionId())
                    .wordId(definition.getWord().getWordId())
                    .definition(definition.getDefinition())
                    .type(definition.getType().getDescription())
                    .build();
        }
    }
}