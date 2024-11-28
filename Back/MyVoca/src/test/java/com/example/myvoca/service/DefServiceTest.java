package com.example.myvoca.service;

import com.example.myvoca.dto.CreateDef;
import com.example.myvoca.dto.DefDto;
import com.example.myvoca.entity.Def;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.DefRepository;
import com.example.myvoca.repository.WordRepository;
import com.example.myvoca.type.POS;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefServiceTest {
    @Mock
    private DefRepository defRepository;
    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private DefService defService;

    private final Word defaultWord = Word.builder()
            .wordId(1)
            .expression("asd")
            .build();

    private final Def defaultDef = Def.builder()
            .defId(1)
            .word(defaultWord)
            .definition("asd")
            .type(POS.NOUN)
            .build();

    @DisplayName("[Service] 뜻 가져오기 성공")
    @Test
    void getDefsTest_success() {
        //given
        given(wordRepository.findById(anyInt()))
                .willReturn(Optional.of(defaultWord));
        given(defRepository.findByWord(defaultWord))
                .willReturn(Collections.singletonList(defaultDef));
        //when
        List<DefDto> defs =
                defService.getDefs(1);
        //then
        assertEquals(defs.get(0).getDefinition(), defaultDef.getDefinition());
    }

    @DisplayName("[Service] 뜻 추가 성공")
    @Test
    void createDefTest_success() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(defRepository.save(any(Def.class)))
                .willReturn(defaultDef);
        ArgumentCaptor<Def> captor =
                ArgumentCaptor.forClass(Def.class);
        //when
        defService.createDef(
                1, toRequest(defaultDef)
        );
        //then
        verify(defRepository, times(1))
                .save(captor.capture());
        Def savedDef = captor.getValue();
        assertEquals(savedDef.getDefinition(),
                defaultDef.getDefinition());
    }

    @DisplayName("[Service] 뜻 추가 시 중복 체크")
    @Test
    void createDefTest_duplicate(){
        //given
        CreateDef.Request request = toRequest(defaultDef);
        given(defRepository.findByDefinitionAndWord(anyString()
                , any(Word.class)))
                .willReturn(Optional.of(defaultDef));
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            defService.createDef(1, request);
        });
        //then
        assertEquals(e.getMessage(), DUPLICATED_DEFINITION.getMessage());
    }
            
    @DisplayName("[Service] 뜻 생성 NO_WORD")
    @Test
    void createDefTest_no_word() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            defService.createDef(
                    1, toRequest(defaultDef)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 뜻 수정 NO_DEFINITION")
    @Test
    void editDefinitionTest_no_def() {
        //given
        given(defRepository.findById(1))
                .willReturn(Optional.empty());
        CreateDef.Request request = toRequest(defaultDef);
        request.setDefinition("ZZZ");
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            defService.editDef(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_DEF.getMessage());
    }
    
    @DisplayName("[Service] 뜻 삭제 성공")
    @Test
    void deleteDefTest_success() {
        //given
        given(defRepository.findById(1))
                .willReturn(Optional.of(defaultDef));
        //when
        DefDto deletedDefinition = defService.deleteDef(1);
        //then
        assertEquals(deletedDefinition.getDefinition(), defaultDef.getDefinition());
        assertEquals(deletedDefinition.getType(), defaultDef.getType().getType());
    }

    private CreateDef.Request toRequest(Def def) {
        return CreateDef.Request.builder()
                .definition(def.getDefinition())
                .type(def.getType())
                .build();
    }
}