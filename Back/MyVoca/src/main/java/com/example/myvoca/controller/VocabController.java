package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateVocab;
import com.example.myvoca.dto.EditVocab;
import com.example.myvoca.dto.VocabDto;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.VocabWord;
import com.example.myvoca.entity.Word;
import com.example.myvoca.service.VocabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VocabController{
    private final VocabService vocabService;

    @GetMapping("/api/vocabs/all")
    public List<VocabDto> getVocabByUserId(
            @Valid @RequestParam Integer userId
    ){
        return vocabService.getVocabByUserId(userId);
    };

    @PostMapping("/api/vocabs")
    public CreateVocab.Response createVocab(
            @Valid @RequestBody CreateVocab.Request request
    ){
        return vocabService.createVocab(request);
    }

    @PatchMapping("/api/vocabs/{vocabId}")
    public VocabDto editVocab(
            @Valid @PathVariable Integer vocabId,
            @Valid @RequestBody EditVocab.Request request
    ){
        return vocabService.editVocab(vocabId, request);
    }

    @DeleteMapping("/api/vocabs/{vocabId}")
    public VocabDto deleteVocab(
            @Valid @PathVariable Integer vocabId
    ){
        return vocabService.deleteVocab(vocabId);
    }
}