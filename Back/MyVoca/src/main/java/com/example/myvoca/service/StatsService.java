package com.example.myvoca.service;

import com.example.myvoca.dto.StatsDto;
import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.entity.Stats;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.StatsRepository;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.WordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.myvoca.code.ApiResponseCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatsService {
    private final StatsRepository statsRepository;
    private final VocabRepository vocabRepository;
    private final WordRepository wordRepository;

    @Transactional
    public UpdateStats.Response updateStats(Integer wordId, UpdateStats.Request request) {
        Optional<Stats> OpStats = statsRepository.findById(wordId);

        if(OpStats.isEmpty()){
            //Stats을 최초 생성하는 경우
            if (request.getIsLearned() == null)
                request.setIsLearned(0);
            if (request.getCorrectCount() == null)
                request.setCorrectCount(0);
            if (request.getIncorrectCount() == null)
                request.setIncorrectCount(0);
            Stats stats = Stats.builder()
                    .word(getWordById(wordId))
                    .correctCount(request.getCorrectCount())
                    .incorrectCount(request.getIncorrectCount())
                    .isLearned(request.getIsLearned())
                    .build();

            return UpdateStats.Response.fromEntity(statsRepository.save(stats));
        }

        //Stats이 이미 존재하는 경우
        Stats stats = OpStats.get();
        if (request.getIsLearned() != null)
            stats.setIsLearned(request.getIsLearned());
        if (request.getIncorrectCount() != null)
            stats.setIncorrectCount(request.getIncorrectCount());
        if (request.getCorrectCount() != null)
            stats.setCorrectCount(request.getCorrectCount());

        return UpdateStats.Response.fromEntity(stats);
    }

    public StatsDto getStatsDetail(Integer wordId) {
        return StatsDto.fromEntity(getStatsById(wordId));
    }

    public double getDifficulty(Integer wordId) {
        Stats stats = getStatsById(wordId);
        return calcDifficulty(stats.getCorrectCount(), stats.getIncorrectCount());
    }

    public double getLearningRate(Integer vocabId) {
        return statsRepository.getLearningRateByVocab(getVocabById(vocabId));
    }

    public List<StatsDto> getStatsByVocabId(Integer vocabId) {
        return statsRepository.findByVocab(getVocabById(vocabId))
                .stream().map(StatsDto::fromEntity)
                .collect(Collectors.toList());
    }

    private Stats getStatsById(Integer wordId) {
        return statsRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(NO_STATS));
    }

    private Vocab getVocabById(Integer vocabId) {
        return vocabRepository.findById(vocabId)
                .orElseThrow(() -> new ApiException(NO_VOCAB));
    }

    private Word getWordById(Integer wordId) {
        return wordRepository.findById(wordId)
                .orElseThrow(() -> new ApiException(NO_WORD));
    }

    private Double calcDifficulty(Integer correct, Integer incorrect) {
        /*
            난이도 함수 : -0.04correct + 0.05incorrect + 0.5
        */
        double diff = (-0.04 * correct) + (0.05 * incorrect) + 0.5;
        if (diff > 1)
            diff = 1;
        else if (diff < 0)
            diff = 0;
        return diff;
    }
}