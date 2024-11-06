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

    @PostMapping("/api/words")
    public CreateWord.Response createWord(
            @Valid @RequestBody CreateWord.Request request
    ){
        return wordService.createWord(request);
    }

    @PatchMapping("/api/words/{wordId}")
    public WordDto editWord(
            @Valid @PathVariable Integer wordId,
            @Valid @RequestBody EditWord.Request request
    ){
        return wordService.editWord(wordId, request);
    }

    @DeleteMapping("/api/words/{wordId}")
    public WordDto deleteWord(
            @Valid @PathVariable Integer wordId
    ){
        return wordService.deleteWord(wordId);
    }
}