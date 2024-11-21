package com.example.myvoca.controller;

import com.example.myvoca.code.ApiResponseCode;
import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.CreateWord;
import com.example.myvoca.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class WordController{
    private final WordService wordService;

    @GetMapping("/words/all")
    public ResponseEntity<?> getWordByVocabId(
            @Valid @RequestParam Integer vocab_id
    ){
        log.info("HTTP GET /api/words/all?vocab_id="+vocab_id);
        return ApiResponse.toResponseEntity(ApiResponseCode.OK,
                wordService.getWords(vocab_id));
    }

    @GetMapping("/words/detail")
    public ResponseEntity<?> getWordByWordId(
            @Valid @RequestParam Integer word_id
    ){
        log.info("HTTP GET /api/words/detail?word_id="+word_id);
        return ApiResponse.toResponseEntity(ApiResponseCode.OK,
                wordService.getWordDtoById(word_id));
    }

    @PostMapping("/words/{vocab_id}")
    public ResponseEntity<?> createWord(
            @Valid @PathVariable Integer vocab_id,
            @Valid @RequestBody CreateWord.Request request
    ){
        log.info("HTTP POST /api/words/"+vocab_id);
        return ApiResponse.toResponseEntity(ApiResponseCode.OK,
                wordService.createWord(vocab_id, request));
    }

    @PatchMapping("/words/{word_id}")
    public ResponseEntity<?> editWord(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody CreateWord.Request request
    ){
        log.info("HTTP PATCH /api/words/"+word_id);
        return ApiResponse.toResponseEntity(ApiResponseCode.OK,
                wordService.editWord(word_id, request));
    }

    @DeleteMapping("/words/{word_id}")
    public ResponseEntity<?> deleteWord(
            @Valid @PathVariable Integer word_id
    ){
        log.info("HTTP DELETE /api/words/"+word_id);
        return ApiResponse.toResponseEntity(ApiResponseCode.OK,
                wordService.deleteWord(word_id));
    }
}