package com.example.myvoca.dto;

import com.example.myvoca.entity.Def;
import com.example.myvoca.type.POS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefDto {
    private Integer defId;
    private Integer wordId;
    private String definition;
    private POS type;
    public static DefDto fromEntity(Def def){
        return DefDto.builder()
                .defId(def.getDefId())
                .definition(def.getDefinition())
                .wordId(def.getWord().getWordId())
                .type(def.getType())
                .build();
    }
}
