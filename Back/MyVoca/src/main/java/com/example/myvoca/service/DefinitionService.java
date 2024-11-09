package com.example.myvoca.service;

import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.dto.DefinitionDto;
import com.example.myvoca.entity.Definition;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.DefinitionRepository;
import com.example.myvoca.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public DefinitionDto createDefinition(Integer wordId, CreateDefinition.Request request) {
        Definition definition = Definition.builder()
                .word(getWordById(wordId))
                .definition(request.getDefinition())
                .type(request.getType())
                .build();
        definition = definitionRepository.save(definition);

        return DefinitionDto.fromEntity(definition);
    }

    private Word getWordById(Integer wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(NoSuchElementException::new);
        return word;
    }
}
