package com.example.myvoca.dto;

import com.example.myvoca.entity.Def;
import com.example.myvoca.type.POS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateDef {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotBlank
        @Size(max=64)
        private String definition;
        @NotNull //Enumerated Variable 의 경우 NotBlank 안됨, Not Null!
        private POS type;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response{
        private Integer defId;
        private Integer wordId;
        private String definition;
        private String type;
        public static Response fromEntity(Def def){
            return Response.builder()
                    .defId(def.getDefId())
                    .wordId(def.getWord().getWordId())
                    .definition(def.getDefinition())
                    .type(def.getType().getType())
                    .build();
        }
    }
}