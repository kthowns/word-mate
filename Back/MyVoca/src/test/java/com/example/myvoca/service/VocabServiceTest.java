package com.example.myvoca.service;

import com.example.myvoca.dto.CreateVocab;
import com.example.myvoca.dto.VocabDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.repository.UserRepository;
import com.example.myvoca.repository.VocabRepository;
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
class VocabServiceTest {
    @Mock
    private VocabRepository vocabRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VocabService vocabService;

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

    @DisplayName("[Service] 단어장 가져오기 성공")
    @Test
    void getVocabsTest_success() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.of(defaultUser));
        given(vocabRepository.findByUser(defaultUser))
                .willReturn(Collections.singletonList(defaultVocab));
        //when
        List<VocabDto> vocabs =
                vocabService.getVocabs(1);
        //then
        assertEquals(vocabs.get(0).getVocabId(), defaultVocab.getVocabId());
        assertEquals(vocabs.get(0).getTitle(), defaultVocab.getTitle());
        assertEquals(vocabs.get(0).getDescription(), defaultVocab.getDescription());
    }

    @DisplayName("[Service] 단어장 추가 성공")
    @Test
    void createVocabTest_success() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.of(defaultUser));
        given(vocabRepository.save(any(Vocab.class)))
                .willReturn(defaultVocab);
        ArgumentCaptor<Vocab> captor =
                ArgumentCaptor.forClass(Vocab.class);
        //when
        vocabService.createVocab(
                1, toRequest(defaultVocab)
        );
        //then
        verify(vocabRepository, times(1))
                .save(captor.capture());
        Vocab savedVocab = captor.getValue();
        assertEquals(savedVocab.getTitle(), defaultVocab.getTitle());
        assertEquals(savedVocab.getDescription(), defaultVocab.getDescription());
    }

    @DisplayName("[Service] 단어장 추가 시 중복 체크")
    @Test
    void createVocabTest_duplicate(){
        //given
        CreateVocab.Request request = toRequest(defaultVocab);
        given(vocabRepository.findByTitleAndUser(anyString()
                , any(User.class)))
                .willReturn(Optional.of(defaultVocab));
        given(userRepository.findById(1))
                .willReturn(Optional.of(defaultUser));
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            vocabService.createVocab(1, request);
        });
        //then
        assertEquals(e.getMessage(), DUPLICATED_TITLE.getMessage());
    }
            
    @DisplayName("[Service] 단어장 생성 NO_USER")
    @Test
    void createVocabTest_no_user() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            vocabService.createVocab(
                    1, toRequest(defaultVocab)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_USER.getMessage());
    }

    @DisplayName("[Service] 단어장 수정 NO_VOCAB")
    @Test
    void editVocabTest_no_vocab() {
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.empty());
        CreateVocab.Request request = toRequest(defaultVocab);
        request.setTitle("ZZZ");
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            vocabService.editVocab(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_VOCAB.getMessage());
    }
    
    @DisplayName("[Service] 단어장 삭제 성공")
    @Test
    void deleteVocabTest_success() {
        //given
        given(vocabRepository.findById(1))
                .willReturn(Optional.of(defaultVocab));
        //when
        VocabDto deletedVocab = vocabService.deleteVocab(1);
        //then
        assertEquals(deletedVocab.getVocabId(), defaultVocab.getVocabId());
        assertEquals(deletedVocab.getTitle(), defaultVocab.getTitle());
        assertEquals(deletedVocab.getDescription(), defaultVocab.getDescription());
    }

    private CreateVocab.Request toRequest(Vocab vocab) {
        return CreateVocab.Request.builder()
                .title(vocab.getTitle())
                .description(vocab.getDescription())
                .build();
    }
}