package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateWord;
import com.example.myvoca.dto.EditWord;
import com.example.myvoca.dto.WordDto;
import com.example.myvoca.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WordController{
    private final WordService wordService;

    @GetMapping("/api/words/all")
    public List<WordDto> getWordByVocabId(
            @Valid @RequestParam Integer vocab_id
    ){
        return wordService.getWordByVocabId(vocab_id);
    }

    @GetMapping("/api/words/detail")
    public WordDto getWordByWordId(
            @Valid @RequestParam Integer word_id
    ){
        return wordService.getWordDtoById(word_id);
    }

    @PostMapping("/api/words/{vocab_id}")
    public CreateWord.Response createWord(
            @Valid @PathVariable Integer vocab_id,
            @Valid @RequestBody CreateWord.Request request
    ){
        return wordService.createWord(vocab_id, request);
    }

    @PatchMapping("/api/words/{word_id}")
    public WordDto editWord(
            @Valid @PathVariable Integer word_id,
            @Valid @RequestBody EditWord.Request request
    ){
        return wordService.editWord(word_id, request);
    }

    @DeleteMapping("/api/words/{word_id}")
    public WordDto deleteWord(
            @Valid @PathVariable Integer word_id
    ){
        return wordService.deleteWord(word_id);
    }
}