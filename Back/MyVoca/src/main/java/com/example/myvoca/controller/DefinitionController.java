package com.example.myvoca.controller;

import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.service.DefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.myvoca.code.ApiResponseCode.OK;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class DefinitionController {
    private final DefinitionService definitionService;

    @GetMapping("/defs/all")
    public ResponseEntity<?> getDefinitions(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/all/def/" + word_id);

        return ApiResponse.toResponseEntity(OK, definitionService.getDefinitions(word_id));
    }

    @PostMapping("/defs/{word_id}")
    public ResponseEntity<?> createDefinition(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody CreateDefinition.Request request
    ) {
        log.info("HTTP POST /api/def/" + word_id);
        return ApiResponse.toResponseEntity(OK, definitionService.createDefinition(word_id, request));
    }

    @PatchMapping("/defs/{def_id}")
    public ResponseEntity<?> editDefinition(
            @Valid @PathVariable Integer def_id,
            @Valid @RequestBody CreateDefinition.Request request
    ) {
        log.info("HTTP POST /api/def/" + def_id);
        return ApiResponse.toResponseEntity(OK, definitionService.editDefinition(def_id, request));
    }
}