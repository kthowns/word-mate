package com.example.myvoca.service;

import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.dto.DefinitionDto;
import com.example.myvoca.entity.Definition;
import com.example.myvoca.entity.Word;
import com.example.myvoca.repository.DefinitionRepository;
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
class DefinitionServiceTest {
    @Mock
    private DefinitionRepository definitionRepository;
    @Mock
    private WordRepository wordRepository;

    @InjectMocks
    private DefinitionService definitionService;

    private final Word defaultWord = Word.builder()
            .wordId(1)
            .expression("asd")
            .build();

    private final Definition defaultDef = Definition.builder()
            .definitionId(1)
            .word(defaultWord)
            .definition("asd")
            .type(POS.NOUN)
            .build();

    @DisplayName("[Service] 뜻 가져오기 성공")
    @Test
    void getDefinitionsTest_success() {
        //given
        given(wordRepository.findById(anyInt()))
                .willReturn(Optional.of(defaultWord));
        given(definitionRepository.findByWord(defaultWord))
                .willReturn(Collections.singletonList(defaultDef));
        //when
        List<DefinitionDto> defs =
                definitionService.getDefinitions(1);
        //then
        assertEquals(defs.get(0).getDefinition(), defaultDef.getDefinition());
    }

    @DisplayName("[Service] 뜻 추가 성공")
    @Test
    void createDefinitionTest_success() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        given(definitionRepository.save(any(Definition.class)))
                .willReturn(defaultDef);
        ArgumentCaptor<Definition> captor =
                ArgumentCaptor.forClass(Definition.class);
        //when
        definitionService.createDefinition(
                1, toRequest(defaultDef)
        );
        //then
        verify(definitionRepository, times(1))
                .save(captor.capture());
        Definition savedDefinition = captor.getValue();
        assertEquals(savedDefinition.getDefinition(),
                defaultDef.getDefinition());
    }

    @DisplayName("[Service] 뜻 추가 시 중복 체크")
    @Test
    void createDefinitionTest_duplicate(){
        //given
        CreateDefinition.Request request = toRequest(defaultDef);
        given(definitionRepository.findByDefinitionAndWord(anyString()
                , any(Word.class)))
                .willReturn(Optional.of(defaultDef));
        given(wordRepository.findById(1))
                .willReturn(Optional.of(defaultWord));
        //when
        Throwable e = assertThrows(Exception.class, () -> {
            definitionService.createDefinition(1, request);
        });
        //then
        assertEquals(e.getMessage(), DUPLICATED_DEFINITION.getMessage());
    }
            
    @DisplayName("[Service] 뜻 생성 NO_WORD")
    @Test
    void createDefinitionTest_no_word() {
        //given
        given(wordRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            definitionService.createDefinition(
                    1, toRequest(defaultDef)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_WORD.getMessage());
    }

    @DisplayName("[Service] 뜻 수정 NO_DEFINITION")
    @Test
    void editDefinitionTest_no_definition() {
        //given
        given(definitionRepository.findById(1))
                .willReturn(Optional.empty());
        CreateDefinition.Request request = toRequest(defaultDef);
        request.setDefinition("ZZZ");
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            definitionService.editDefinition(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_DEFINITION.getMessage());
    }
    
    @DisplayName("[Service] 뜻 삭제 성공")
    @Test
    void deleteDefinitionTest_success() {
        //given
        given(definitionRepository.findById(1))
                .willReturn(Optional.of(defaultDef));
        //when
        DefinitionDto deletedDefinition = definitionService.deleteDefinition(1);
        //then
        assertEquals(deletedDefinition.getDefinition(), defaultDef.getDefinition());
        assertEquals(deletedDefinition.getType(), defaultDef.getType().getType());
    }

    private CreateDefinition.Request toRequest(Definition definition) {
        return CreateDefinition.Request.builder()
                .definition(definition.getDefinition())
                .type(definition.getType())
                .build();
    }
}