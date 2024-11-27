package com.example.myvoca.service;

import com.example.myvoca.dto.StatDto;
import com.example.myvoca.dto.UpdateStat;
import com.example.myvoca.entity.Stat;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.exception.ApiException;
import com.example.myvoca.repository.StatRepository;
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
public class StatService {
    private final StatRepository statRepository;
    private final VocabRepository vocabRepository;
    private final WordRepository wordRepository;

    public List<StatDto> getStats(Integer vocabId) {
        return statRepository.findByVocab(getVocabById(vocabId))
                .stream().map(StatDto::fromEntity)
                .collect(Collectors.toList());
    }

    public StatDto getStatDetail(Integer wordId) {
        return StatDto.fromEntity(getStatsById(wordId));
    }

    public double getDifficulty(Integer wordId) {
        Stat stat = getStatsById(wordId);
        return calcDifficulty(stat.getCorrectCount(), stat.getIncorrectCount());
    }

    public double getLearningRate(Integer vocabId) {
        return statRepository.getLearningRateByVocab(getVocabById(vocabId));
    }

    @Transactional
    public UpdateStat.Response updateStat(Integer wordId, UpdateStat.Request request) {
        Optional<Stat> OpStat = statRepository.findById(wordId);

        if(OpStat.isEmpty()){
            //Stat을 최초 생성하는 경우
            if (request.getIsLearned() == null)
                request.setIsLearned(0);
            if (request.getCorrectCount() == null)
                request.setCorrectCount(0);
            if (request.getIncorrectCount() == null)
                request.setIncorrectCount(0);
            Stat stat = Stat.builder()
                    .word(getWordById(wordId))
                    .correctCount(request.getCorrectCount())
                    .incorrectCount(request.getIncorrectCount())
                    .isLearned(request.getIsLearned())
                    .build();

            return UpdateStat.Response.fromEntity(statRepository.save(stat));
        }

        //Stat이 이미 존재하는 경우
        Stat stat = OpStat.get();
        if (request.getIsLearned() != null)
            stat.setIsLearned(request.getIsLearned());
        if (request.getIncorrectCount() != null)
            stat.setIncorrectCount(request.getIncorrectCount());
        if (request.getCorrectCount() != null)
            stat.setCorrectCount(request.getCorrectCount());

        return UpdateStat.Response.fromEntity(stat);
    }

    private Stat getStatsById(Integer wordId) {
        return statRepository.findById(wordId)
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