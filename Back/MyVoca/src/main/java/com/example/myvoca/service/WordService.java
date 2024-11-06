package com.example.myvoca.service;

import com.example.myvoca.dto.*;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.VocabWord;
import com.example.myvoca.entity.VocabWordId;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.VocabWordRepository;
import com.example.myvoca.repository.WordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class WordService {
    private final VocabRepository vocabRepository;
    private final WordRepository wordRepository;
    private final VocabWordRepository vocabWordRepository;

    public List<WordDto> getWordByVocabId(Integer vocabId) {
        return wordRepository.findWordByVocabId(getVocabById(vocabId).getVocabId())
                .stream().map(WordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateWord.Response createWord(CreateWord.Request request) {
        Word word = Word.builder()
                .expression(request.getExpression())
                .build();
        word = wordRepository.save(word);

        Vocab vocab = getVocabById(request.getVocabId());
        VocabWord vocabWord = new VocabWord();
        vocabWord.setVocabWordId(
                new VocabWordId(word.getWordId(), vocab.getVocabId())
        );
        vocabWord.setWord(word);
        vocabWord.setVocab(vocab);
        vocabWordRepository.save(vocabWord);

        vocab.setWordCount(getWordByVocabId(vocab.getVocabId()).size());

        return CreateWord.Response.fromEntity(word);
    }

    public WordDto getWordDtoById(Integer wordId) {
        return WordDto.fromEntity(getWordById(wordId));
    }

    @Transactional
    public WordDto editWord(Integer wordId, EditWord.Request request) {
        Word word = getWordById(wordId);
        word.setExpression(request.getExpression());

        return WordDto.fromEntity(word);
    }

    @Transactional
    public WordDto deleteWord(Integer wordId) {
        Word word = getWordById(wordId);
        log.info(word.toString());
        List<VocabWord> vocabWords = vocabWordRepository.findByWordId(wordId);
        log.info("Before delete");
        wordRepository.delete(word);
        log.info("After delete");
        vocabWords.forEach((vocabWord)->{
            Vocab vocab = vocabWord.getVocab();
            vocab.setWordCount(vocabWordRepository.getCountByVocabId(vocab.getVocabId()));
        });

        return WordDto.fromEntity(word);
    }

    private Vocab getVocabById(Integer vocabId) {
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(NoSuchElementException::new);
        return vocab;
    }

    private Word getWordById(Integer wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(NoSuchElementException::new);
        return word;
    }
}