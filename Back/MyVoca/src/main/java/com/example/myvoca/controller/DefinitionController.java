package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.dto.DefinitionDto;
import com.example.myvoca.service.DefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class DefinitionController {
    private final DefinitionService definitionService;

    @GetMapping("/defs/{word_id}")
    public List<DefinitionDto> getDefinitions(
            @Valid @PathVariable Integer word_id
    ){
        log.info("HTTP GET /api/def/"+word_id);
        return definitionService.getDefinitions(word_id);
    }

    @PostMapping("/defs/{word_id}")
    public DefinitionDto createDefinition(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody CreateDefinition.Request request
    ){
        log.info("HTTP PATCH /api/def/"+word_id);
        return definitionService.createDefinition(word_id, request);
    }
}
