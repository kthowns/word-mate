package com.example.myvoca.controller;

import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.UpdateStat;
import com.example.myvoca.service.StatService;
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
public class StatController {
    private final StatService statService;

    @GetMapping("/stats/all")
    public ResponseEntity<?> getStats(
            @Valid @RequestParam Integer vocab_id
    ) {
        log.info("HTTP GET /api/stats/all?vocab_id=" + vocab_id);
        return ApiResponse.toResponseEntity(OK,
                statService.getStats(vocab_id));
    }

    @GetMapping("/stats/detail")
    public ResponseEntity<?> getStatsDetail(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/stats/detail?word_id=" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statService.getStatDetail(word_id));
    }

    @GetMapping("/stats/lr")
    public ResponseEntity<?> getLearningRate(
            @Valid @RequestParam Integer vocab_id
    ) {
        log.info("HTTP GET /api/stats/lr?vocab_id=" + vocab_id);
        return ApiResponse.toResponseEntity(OK,
                statService.getLearningRate(vocab_id));
    }

    @GetMapping("/stats/diff")
    public ResponseEntity<?> getDifficulty(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/stats/diff?word_id=" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statService.getDifficulty(word_id));
    }

    @PatchMapping("/stats/{word_id}")
    public ResponseEntity<?> updateStats(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody UpdateStat.Request request
    ) {
        log.info("HTTP PUT /api/stats/" + word_id);
        return ApiResponse.toResponseEntity(OK,
                statService.updateStat(word_id, request));
    }
}