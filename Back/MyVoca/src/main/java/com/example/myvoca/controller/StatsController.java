package com.example.myvoca.controller;

import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.dto.WordStatsDto;
import com.example.myvoca.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/api/stats/all")
    public List<WordStatsDto> getWordStatsByVocabId(
            @Valid @RequestParam Integer vocab_id
    ) {
        log.info("HTTP GET /api/stats/all?vocab_id="+vocab_id);
        return statsService.getWordStatsByVocabId(vocab_id);
    }

    @GetMapping("/api/stats/detail")
    public WordStatsDto getWordStatsByWordId(
            @Valid @RequestParam Integer word_id
    ) {
        log.info("HTTP GET /api/stats/detail?word_id="+word_id);
        return statsService.getWordStatsByWordId(word_id);
    }

    @GetMapping("/api/stats/lr")
    public Double getLearningRate(
            @Valid @RequestParam Integer vocab_id
    ){
        log.info("HTTP GET /api/stats/lr?vocab_id="+vocab_id);
        return statsService.getLearningRate(vocab_id);
    }

    @GetMapping("/api/stats/diff")
    public Double getDifficulty(
            @Valid @RequestParam Integer word_id
    ){
        log.info("HTTP GET /api/stats/diff?word_id="+word_id);
        return statsService.getDifficulty(word_id);
    }

    @PutMapping("/api/stats/{word_id}")
    public UpdateStats.Response updateStats(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody UpdateStats.Response request
    ) {
        log.info("HTTP PUT /api/stats/"+word_id);
        return statsService.updateStats(word_id, request);
    }
}