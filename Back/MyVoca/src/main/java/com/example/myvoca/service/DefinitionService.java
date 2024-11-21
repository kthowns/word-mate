package com.example.myvoca.service;

import com.example.myvoca.code.ApiResponseCode;
import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.dto.DefinitionDto;
import com.example.myvoca.entity.Definition;
import com.example.myvoca.entity.Word;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.DefinitionRepository;
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
public class DefinitionService {
    private final DefinitionRepository definitionRepository;
    private final WordRepository wordRepository;

    public List<DefinitionDto> getDefinitions(Integer wordId) {
        return definitionRepository.findByWord(getWordById(wordId))
                .stream().map(DefinitionDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DefinitionDto createDefinition(Integer wordId, CreateDefinition.Request request) {
        validateDefinitionDuplicate(request.getDefinition(), wordId);
        Definition definition = Definition.builder()
                .word(getWordById(wordId))
                .definition(request.getDefinition())
                .type(request.getType())
                .build();
        definition = definitionRepository.save(definition);

        return DefinitionDto.fromEntity(definition);
    }

    @Transactional
    public DefinitionDto editDefinition(Integer defId, CreateDefinition.Request request) {
        Definition definition = getDefinitionById(defId);
        definition.setDefinition(request.getDefinition());
        definition.setType(request.getType());

        return DefinitionDto.fromEntity(definition);
    }

    @Transactional
    public DefinitionDto deleteDefinition(Integer defId) {
        Definition definition = getDefinitionById(defId);
        definitionRepository.delete(definition);

        return DefinitionDto.fromEntity(definition);
    }

    private Definition getDefinitionById(Integer defId){
        return definitionRepository.findById(defId)
                .orElseThrow(() -> new ApiException(ApiResponseCode.NO_DEFINITION));
    }

    private Word getWordById(Integer wordId) {
        return wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(NO_WORD));
    }

    private void validateDefinitionDuplicate(String definition, Integer wordId){
        definitionRepository.findByDefinitionAndWord(definition, getWordById(wordId))
                .ifPresent((e) -> { throw new ApiException(DUPLICATED_DEFINITION); });
    }
}