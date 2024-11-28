package com.example.myvoca.service;

import com.example.myvoca.dto.CreateWord;
import com.example.myvoca.dto.WordDto;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
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

import static com.example.myvoca.code.ApiResponseCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {
    @Mock
    private WordRepository wordRepository;
    @Mock
    private VocabRepository vocabRepository;
    @Mock
    private StatService statService;

    @InjectMocks
    private WordService wordService;

    private final Vocab defaultVocab = Vocab.builder()
            .vocabId(1)
            .wordCount(1)
            .build();

    private final Word defaultWord = Word.builder()
            .wordId(1)
            .vocab(defaultVocab)
            .expression("ASD")
            .build();

    @DisplayName("[Service] 단어 가져오기 성공")
    @Test
    void getWordsTest_success() {
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        given(wordRepository.findByVocab(defaultVocab))
                .willReturn(Collections.singletonList(defaultWord));
        //when
        List<WordDto> vocabs =
                wordService.getWords(1);
        //then
        assertEquals(vocabs.get(0).getWordId(), defaultWord.getWordId());
        assertEquals(vocabs.get(0).getExpression(), defaultWord.getExpression());
    }

    @DisplayName("[Service] 단어 추가 성공")
    @Test
    void createWordTest_success() {
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        given(wordRepository.save(any(Word.class)))
                .willReturn(defaultWord);
        ArgumentCaptor<Word> captor =
                ArgumentCaptor.forClass(Word.class);
        //when
        wordService.createWord(
                1, toRequest(defaultWord)
        );
        //then
        verify(wordRepository, times(1))
                .save(captor.capture());
        Word savedWord = captor.getValue();
        assertEquals(savedWord.getExpression(), defaultWord.getExpression());
    }

    @DisplayName("[Service] 단어 추가 시 중복 체크")
    @Test
    void createWordTest_duplicate(){
        //given
        CreateWord.Request request = toRequest(defaultWord);
        given(wordRepository.findByExpressionAndVocab(anyString()
                , any(Vocab.class)))
                .willReturn(Optional.of(defaultWord));
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            wordService.createWord(1, request);
        });
        //then
        assertEquals(e.getMessage(), DUPLICATED_WORD.getMessage());
    }

    @DisplayName("[Service] 단어 생성 NO_VOCAB")
    @Test
    void createWordTest_no_vocab() {
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            wordService.createWord(
                    1, toRequest(defaultWord)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_VOCAB.getMessage());
    }

    @DisplayName("[Service] 단어 수정 NO_WORD")
    @Test
    void editWordTest_no_word() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        CreateWord.Request request = toRequest(defaultWord);
        request.setExpression("ZZZ");
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            wordService.editWord(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 단어 삭제 성공")
    @Test
    void deleteWordTest_success() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        //when
        WordDto deletedWord = wordService.deleteWord(1);
        //then
        assertEquals(deletedWord.getWordId(), defaultWord.getWordId());
        assertEquals(deletedWord.getExpression(), defaultWord.getExpression());
    }

    private CreateWord.Request toRequest(Word word) {
        return CreateWord.Request.builder()
                .expression(word.getExpression())
                .build();
    }
}