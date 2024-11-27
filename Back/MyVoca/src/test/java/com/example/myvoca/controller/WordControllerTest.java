package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateWord;
import com.example.myvoca.dto.WordDto;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.service.WordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.example.myvoca.code.ApiResponseCode.INTERNAL_SERVER_ERROR;
import static com.example.myvoca.code.ApiResponseCode.INVALID_REQUEST_BODY;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordController.class)
class WordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WordService wordService;

    Vocab defaultVocab = Vocab.builder()
            .vocabId(1)
            .title("title")
            .description("description that over 15 letters")
            .wordCount(1)
            .build();

    Word defaultWord = Word.builder()
            .wordId(1)
            .vocab(defaultVocab)
            .expression("expression")
            .build();

    @DisplayName("[API][GET] /api/words/all 성공")
    @Test
    void getWordsTest_success() throws Exception {
        //given
        List<WordDto> response = Collections.singletonList(
                WordDto.fromEntity(defaultWord));
        given(wordService.getWords(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/words/all")
                        .param("vocab_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].wordId")
                                .value(defaultWord.getWordId())
                ).andExpect(
                        jsonPath("$.data[0].expression")
                                .value(defaultWord.getExpression())
                );
    }

    @DisplayName("[API][GET] /api/words/all No param")
    @Test
    void getWordsTest_no_param() throws Exception {
        //given
        //when & then
        mockMvc.perform(get("/api/words/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][GET] /api/words/detail 성공")
    @Test
    void getWordDetailTest_success() throws Exception {
        //given
        WordDto response = WordDto.fromEntity(defaultWord);
        given(wordService.getWordDetail(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/words/detail")
                        .param("word_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultWord.getWordId())
                ).andExpect(
                        jsonPath("$.data.expression")
                                .value(defaultWord.getExpression())
                );
    }

    @DisplayName("[API][GET] /api/words/detail No param")
    @Test
    void getWordDetailTest_no_param() throws Exception {
        //given
        //when & then
        mockMvc.perform(get("/api/words/detail"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][POST] /api/words/{user_id} 성공")
    @Test
    void createWordTest_success() throws Exception {
        CreateWord.Request request = toRequest(defaultWord);
        CreateWord.Response response = CreateWord.Response
                .fromEntity(defaultWord);
        //given
        given(wordService.createWord(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/words/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultWord.getWordId())
                ).andExpect(
                        jsonPath("$.data.expression")
                                .value(defaultWord.getExpression())
                );
    }

    @DisplayName("[API][POST] /api/words/{user_id} Invalid request")
    @Test
    void createWordTest_invalid_request() throws Exception {
        CreateWord.Request request = toRequest(defaultWord);
        CreateWord.Response response = CreateWord.Response
                .fromEntity(defaultWord);
        request.setExpression(null);
        //given
        given(wordService.createWord(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/words/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INVALID_REQUEST_BODY.getMessage())
                );
    }

    @DisplayName("[API][PATCH] /api/words/{vocab_id} 성공")
    @Test
    void editWordTest_success() throws Exception {
        CreateWord.Request request = toRequest(defaultWord);
        request.setExpression("Hello");
        CreateWord.Response response = CreateWord.Response.fromEntity(defaultWord);
        response.setExpression(request.getExpression());
        //given
        given(wordService.editWord(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/words/{vocab_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultWord.getWordId())
                ).andExpect(
                        jsonPath("$.data.expression")
                                .value(request.getExpression())
                );
    }

    @DisplayName("[API][DELETE] /api/words/{vocab_id} 성공")
    @Test
    void deleteWordTest_success() throws Exception {
        //given
        given(wordService.deleteWord(1))
                .willReturn(WordDto.fromEntity(defaultWord));
        //when & then
        mockMvc.perform(delete("/api/words/{vocab_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultWord.getWordId())
                ).andExpect(
                        jsonPath("$.data.expression")
                                .value(defaultWord.getExpression())
                );
    }

    private CreateWord.Request toRequest(Word word) {
        return CreateWord.Request.builder()
                .expression(word.getExpression())
                .build();
    }
}