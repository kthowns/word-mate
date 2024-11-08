package com.example.myvoca.service;

import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.dto.WordStatsDto;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.entity.WordStats;
import com.example.myvoca.repository.StatsRepository;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;
    private final VocabRepository vocabRepository;
    private final WordRepository wordRepository;

    public UpdateStats.Response updateStats(Integer wordId, UpdateStats.Response request) {
        if (request.getIsLearned() == null)
            request.setIsLearned(0);
        if (request.getCorrectCount() == null)
            request.setCorrectCount(0);
        if (request.getIncorrectCount() == null)
            request.setIncorrectCount(0);
        WordStats wordStats = WordStats.builder()
                .wordId(wordId)
                .correctCount(request.getCorrectCount())
                .incorrectCount(request.getIncorrectCount())
                .isLearned(request.getIsLearned())
                .build();
        wordStats = statsRepository.save(wordStats);
        return UpdateStats.Response.fromEntity(wordStats);
    }

    public WordStatsDto getWordStatsByWordId(Integer wordId) {
        return WordStatsDto.fromEntity(findWordStatsById(wordId));
    }

    public double getDifficulty(Integer wordId){
        WordStats wordStats = findWordStatsById(wordId);
        return calcDifficulty(wordStats.getCorrectCount(), wordStats.getIncorrectCount());
    }

    public WordStats findWordStatsById(Integer wordId){
        return statsRepository.findById(wordId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Double getLearningRate(Integer vocabId) {
        Vocab vocab = vocabRepository.findById(vocabId)
                .orElseThrow(NoSuchElementException::new);
        List<Word> wordList = wordRepository.findWordsByVocabId(vocabId);
        Double cntLearnedWord = 0.0;
        for (Word word : wordList) {
            WordStats wordStats = statsRepository.findById(word.getWordId())
                    .orElseThrow(NoSuchElementException::new);
            if (wordStats.getIsLearned() == 1)
                cntLearnedWord++;
        }
        log.info("Learned Word : "+cntLearnedWord+" / wordCount : " +vocab.getWordCount());
        return cntLearnedWord/vocab.getWordCount();
    }

    private Double calcDifficulty(Integer correct, Integer incorrect){
        /*
            난이도 함수 : -0.04correct + 0.05incorrect + 0.5
        */
        double diff = (-0.04 * correct) + (0.05 * incorrect) + 0.5;
        if(diff > 1)
            diff = 1;
        else if(diff < 0)
            diff = 0;
        return diff;
    }

    public List<WordStatsDto> getWordStatsByVocabId(Integer vocabId) {
        return statsRepository.findWordStatesByVocabId(vocabId)
                .stream().map(WordStatsDto::fromEntity)
                .collect(Collectors.toList());
    }
}