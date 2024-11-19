package com.example.myvoca.controller;

import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.service.StatsService;
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
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats/all")
    public ResponseEntity<?> getWordStatsByVocabId(
            @Valid @RequestParam Integer vocab_id
    ) {
        log.info("HTTP GET /api/stats/all?vocab_id=" + vocab_id);
        return ApiResponse.toResponseEntity(OK,
                statsService.getWordStatsByVocabId(vocab_id));
    }

    @GetMapping("/stats/detail")
    public ResponseEntity<?> getWordStatsByWordId(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/stats/detail?word_id=" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statsService.getWordStatsByWordId(word_id));
    }

    @GetMapping("/stats/lr")
    public ResponseEntity<?> getLearningRate(
            @Valid @RequestParam Integer vocab_id
    ) {
        log.info("HTTP GET /api/stats/lr?vocab_id=" + vocab_id);
        return ApiResponse.toResponseEntity(OK,
                statsService.getLearningRate(vocab_id));
    }

    @GetMapping("/stats/diff")
    public ResponseEntity<?> getDifficulty(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/stats/diff?word_id=" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statsService.getDifficulty(word_id));
    }

    @PutMapping("/stats/{word_id}")
    public ResponseEntity<?> updateStats(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody UpdateStats.Request request
    ) {
        log.info("HTTP PUT /api/stats/" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statsService.updateStats(word_id, request));
    }
}