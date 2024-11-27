package com.example.myvoca.service;

import com.example.myvoca.dto.StatDto;
import com.example.myvoca.dto.UpdateStat;
import com.example.myvoca.entity.Stat;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.StatRepository;
import com.example.myvoca.repository.VocabRepository;
import com.example.myvoca.repository.WordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatServiceTest {
    @Mock
    private VocabRepository vocabRepository;
    @Mock
    private WordRepository wordRepository;
    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private StatService statService;

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

    private final Stat defaultStat = Stat.builder()
            .word(defaultWord)
            .correctCount(6)
            .incorrectCount(3)
            .isLearned(1)
            .build();

    @DisplayName("[Service] 통계 최초 등록")
    @Test
    void updateStatTest_initial() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(statRepository.save(any(Stat.class)))
                .willReturn(defaultStat);
        ArgumentCaptor<Stat> captor =
                ArgumentCaptor.forClass(Stat.class);
        UpdateStat.Request request = toRequest(defaultStat);
        request.setIncorrectCount(null);
        request.setCorrectCount(null);
        request.setIsLearned(null);
        //when
        statService.updateStat(1, request);
        //then
        verify(statRepository, times(1))
                .save(captor.capture());
        Stat savedStat = captor.getValue();
        assertEquals(0, savedStat.getCorrectCount());
        assertEquals(0, savedStat.getIncorrectCount());
        assertEquals(0, savedStat.getIsLearned());
    }

    @DisplayName("[Service] 통계 업데이트 성공")
    @Test
    void updateStatTest_success() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(statRepository.save(any(Stat.class)))
                .willReturn(defaultStat);
        UpdateStat.Request request = toRequest(defaultStat);
        //when
        UpdateStat.Response response = statService.updateStat(1, request);
        //then
        assertEquals(response.getCorrectCount(), defaultStat.getCorrectCount());
        assertEquals(response.getIncorrectCount(), defaultStat.getIncorrectCount());
        assertEquals(response.getIsLearned(), defaultStat.getIsLearned());
        assertEquals(response.getWordId(), defaultStat.getWord().getWordId());
    }

    @DisplayName("[Service] 통계 업데이트 단일 변수")
    @Test
    void updateStatTest_success_single() {
        UpdateStat.Request request = toRequest(defaultStat);
        request.setIncorrectCount(null);
        request.setIsLearned(null);

        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(statRepository.save(any(Stat.class)))
                .willReturn(defaultStat);
        //when
        UpdateStat.Response response = statService.updateStat(1, request);
        //then
        assertEquals(response.getCorrectCount(), defaultStat.getCorrectCount());
        assertEquals(response.getIncorrectCount(), defaultStat.getIncorrectCount());
        assertEquals(response.getIsLearned(), defaultStat.getIsLearned());
        assertEquals(response.getWordId(), defaultStat.getWord().getWordId());
    }

    @DisplayName("[Service] 통계 업데이트 NO_WORD")
    @Test
    void createVocabTest_no_user() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, () ->
                statService.updateStat(1, toRequest(defaultStat))
        );
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 통계 업데이트 NO_WORD ")
    @Test
    void updateStatTest_no_word() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        UpdateStat.Request request = toRequest(defaultStat);
        //when
        Throwable e = assertThrows(Exception.class, () ->
                statService.updateStat(1, request)
        );
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 통계 Detail 성공")
    @Test
    void getStatDetailTest_success(){
        //given
        given(statRepository.findById(1))
                .willReturn(Optional.of(defaultStat));
        //when
        StatDto stats = statService.getStatDetail(1);
        //then
        assertEquals(stats.getWordId(), defaultStat.getWord().getWordId());
    }

    @DisplayName("[Service] 단어 난이도 받기 성공")
    @Test
    void getDifficultyTest_success(){
        //given
        given(statRepository.findById(1))
                .willReturn(Optional.of(defaultStat));
        //when
        Double diff = statService.getDifficulty(1);
        //then
        assertEquals(0.41000000000000003, diff);
    }

    @DisplayName("[Service] 단어장의 모든 통계 가져오기 성공")
    @Test
    void getStatsTest_success(){
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        given(statRepository.findByVocab(any(Vocab.class)))
                .willReturn(Collections.singletonList(defaultStat));
        //when
        List<StatDto> statsList = statService.getStats(1);
        //then
        assertEquals(defaultStat.getWord().getWordId(), statsList.get(0).getWordId());
        assertEquals(defaultStat.getIsLearned(), statsList.get(0).getIsLearned());
        assertEquals(defaultStat.getCorrectCount(), statsList.get(0).getCorrectCount());
        assertEquals(defaultStat.getIncorrectCount(), statsList.get(0).getIncorrectCount());
    }

    private UpdateStat.Request toRequest(Stat stat) {
        return UpdateStat.Request.builder()
                .correctCount(stat.getCorrectCount())
                .incorrectCount(stat.getIncorrectCount())
                .isLearned(stat.getIsLearned())
                .build();
    }
}