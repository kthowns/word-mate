package com.example.myvoca.service;

import com.example.myvoca.dto.StatsDto;
import com.example.myvoca.dto.UpdateStats;
import com.example.myvoca.entity.Stats;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.StatsRepository;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.WordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.myvoca.code.ApiResponseCode.NO_WORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @Mock
    private VocabRepository vocabRepository;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    private final User defaultUser = User.builder()
            .userId(1)
            .username("user01")
            .password("pass01")
            .build();

    private final Vocab defaultVocab = Vocab.builder()
            .vocabId(1)
            .user(defaultUser)
            .wordCount(0)
            .title("TIT")
            .description("DES")
            .build();

    private final Word defaultWord = Word.builder()
            .wordId(1)
            .expression("ASD")
            .vocab(defaultVocab)
            .build();

    private final Stats defaultStats = Stats.builder()
            .word(defaultWord)
            .correctCount(6)
            .incorrectCount(3)
            .isLearned(1)
            .build();

    @DisplayName("[Service] 통계 최초 등록")
    @Test
    void updateStatsTest_initial() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(statsRepository.save(any(Stats.class)))
                .willReturn(defaultStats);
        UpdateStats.Request request = toRequest(defaultStats);
        request.setIncorrectCount(null);
        request.setCorrectCount(null);
        request.setIsLearned(null);
        //when
        UpdateStats.Response response = statsService.updateStats(1, request);
        //then
        assertEquals(defaultStats.getCorrectCount(), response.getCorrectCount());
        assertEquals(defaultStats.getIncorrectCount(), response.getIncorrectCount());
        assertEquals(defaultStats.getIsLearned(), response.getIsLearned());
        assertEquals(defaultStats.getWord().getWordId(), response.getWordId());
    }

    @DisplayName("[Service] 통계 업데이트 성공")
    @Test
    void updateStatsTest_update() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(statsRepository.save(any(Stats.class)))
                .willReturn(defaultStats);
        UpdateStats.Request request = toRequest(defaultStats);
        //when
        UpdateStats.Response response = statsService.updateStats(1, request);
        //then
        assertEquals(response.getCorrectCount(), defaultStats.getCorrectCount());
        assertEquals(response.getIncorrectCount(), defaultStats.getIncorrectCount());
        assertEquals(response.getIsLearned(), defaultStats.getIsLearned());
        assertEquals(response.getWordId(), defaultStats.getWord().getWordId());
    }

    @DisplayName("[Service] 통계 업데이트 NO_WORD")
    @Test
    void createVocabTest_no_user() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            statsService.updateStats(
                    1, toRequest(defaultStats)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 통계 업데이트 NO_WORD ")
    @Test
    void updateStatsTest_no_word() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        UpdateStats.Request request = toRequest(defaultStats);
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            statsService.updateStats(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 통계 Detail 성공")
    @Test
    void getStatsDetailTest_success(){
        //given
        given(statsRepository.findById(1))
                .willReturn(Optional.of(defaultStats));
        //when
        StatsDto stats = statsService.getStatsDetail(1);
        //then
        assertEquals(stats.getWordId(), defaultStats.getWord().getWordId());
    }

    @DisplayName("[Service] 단어 난이도 받기 성공")
    @Test
    void getDifficultyTest_success(){
        //given
        given(statsRepository.findById(1))
                .willReturn(Optional.of(defaultStats));
        //when
        Double diff = statsService.getDifficulty(1);
        //then
        assertEquals(0.41000000000000003, diff);
    }

    @DisplayName("[Service] 단어장의 모든 통계 가져오기 성공")
    @Test
    void getStatsByVocabIdTest_success(){
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        given(statsRepository.findByVocab(any(Vocab.class)))
                .willReturn(Collections.singletonList(defaultStats));
        //when
        List<StatsDto> statsList = statsService.getStatsByVocabId(1);
        //then
        assertEquals(defaultStats.getWord().getWordId(), statsList.get(0).getWordId());
        assertEquals(defaultStats.getIsLearned(), statsList.get(0).getIsLearned());
        assertEquals(defaultStats.getCorrectCount(), statsList.get(0).getCorrectCount());
        assertEquals(defaultStats.getIncorrectCount(), statsList.get(0).getIncorrectCount());
    }

    private UpdateStats.Request toRequest(Stats stats) {
        return UpdateStats.Request.builder()
                .correctCount(stats.getCorrectCount())
                .incorrectCount(stats.getIncorrectCount())
                .isLearned(stats.getIsLearned())
                .build();
    }
}