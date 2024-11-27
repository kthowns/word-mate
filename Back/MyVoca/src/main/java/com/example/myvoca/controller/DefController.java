package com.example.myvoca.controller;

import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.CreateDef;
import com.example.myvoca.service.DefService;
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
public class DefController {
    private final DefService defService;

    @GetMapping("/defs/all")
    public ResponseEntity<?> getDef(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/all/def/" + word_id);

        return ApiResponse.toResponseEntity(OK, defService.getDefs(word_id));
    }

    @PostMapping("/defs/{word_id}")
    public ResponseEntity<?> createDef(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody CreateDef.Request request
    ) {
        log.info("HTTP POST /api/def/" + word_id);
        return ApiResponse.toResponseEntity(OK, defService.createDef(word_id, request));
    }

    @PatchMapping("/defs/{def_id}")
    public ResponseEntity<?> editDef(
            @Valid @PathVariable Integer def_id,
            @Valid @RequestBody CreateDef.Request request
    ) {
        log.info("HTTP PATCH /api/def/" + def_id);
        return ApiResponse.toResponseEntity(OK, defService.editDef(def_id, request));
    }

    @DeleteMapping("/defs/{def_id}")
    public ResponseEntity<?> deleteDef(
            @Valid @PathVariable Integer def_id
    ){
        log.info("HTTP DELETE /api/def/" + def_id);
        return ApiResponse.toResponseEntity(OK, defService.deleteDef(def_id));
    }
}