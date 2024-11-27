package com.example.myvoca.service;

import com.example.myvoca.code.ApiResponseCode;
import com.example.myvoca.dto.CreateDef;
import com.example.myvoca.dto.DefDto;
import com.example.myvoca.entity.Def;
import com.example.myvoca.entity.Word;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.DefRepository;
import com.example.myvoca.repository.WordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.myvoca.code.ApiResponseCode.DUPLICATED_DEFINITION;
import static com.example.myvoca.code.ApiResponseCode.NO_WORD;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefService {
    private final DefRepository defRepository;
    private final WordRepository wordRepository;

    public List<DefDto> getDefs(Integer wordId) {
        return defRepository.findByWord(getWordById(wordId))
                .stream().map(DefDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateDef.Response createDef(Integer wordId, CreateDef.Request request) {
        validateDefDuplicate(request.getDefinition(), getWordById(wordId));
        Def def = Def.builder()
                .word(getWordById(wordId))
                .definition(request.getDefinition())
                .type(request.getType())
                .build();
        def = defRepository.save(def);

        return CreateDef.Response.fromEntity(def);
    }

    @Transactional
    public CreateDef.Response editDef(Integer defId, CreateDef.Request request) {
        Def def = getDefById(defId);
        validateDefDuplicate(request.getDefinition(), def.getWord());
        def.setDefinition(request.getDefinition());
        def.setType(request.getType());

        return CreateDef.Response.fromEntity(def);
    }

    @Transactional
    public DefDto deleteDef(Integer defId) {
        Def def = getDefById(defId);
        defRepository.delete(def);

        return DefDto.fromEntity(def);
    }

    private Def getDefById(Integer defId){
        return defRepository.findById(defId)
                .orElseThrow(() -> new ApiException(ApiResponseCode.NO_DEF));
    }

    private Word getWordById(Integer wordId) {
        return wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(NO_WORD));
    }

    private void validateDefDuplicate(String definition, Word word){
        defRepository.findByDefinitionAndWord(definition, word)
                .ifPresent((e) -> { throw new ApiException(DUPLICATED_DEFINITION); });
    }
}