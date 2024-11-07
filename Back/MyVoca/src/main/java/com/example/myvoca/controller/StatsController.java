package com.example.myvoca.controller;

import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.dto.WordStatsDto;
import com.example.myvoca.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/api/stats/all")
    public List<WordStatsDto> getWordStatsByVocabId(
            @Valid @RequestParam Integer vocab_id
    ) {
        return statsService.getWordStatsByVocabId(vocab_id);
    }

    @GetMapping("/api/stats/detail")
    public WordStatsDto getWordStatsByWordId(
            @Valid @RequestParam Integer word_id
    ) {
        return statsService.getWordStatsByWordId(word_id);
    }

    @GetMapping("/api/stats/lr")
    public Double getLearningRate(
            @Valid @RequestParam Integer vocab_id
    ){
        return statsService.getLearningRate(vocab_id);
    }

    @GetMapping("/api/stats/diff")
    public Double getDifficulty(
            @Valid @RequestParam Integer word_id
    ){
        return statsService.getDifficulty(word_id);
    }

    @PutMapping("/api/stats/{word_id}")
    public UpdateStats.Response updateStats(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody UpdateStats.Response request
    ) {
        return statsService.updateStats(word_id, request);
    }
}