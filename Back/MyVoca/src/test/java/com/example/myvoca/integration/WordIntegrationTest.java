package com.example.myvoca.integration;

import com.example.myvoca.dto.CreateWord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.example.myvoca.code.ApiResponseCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WordIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][GET] 단어 조회 Test")
    @Test
    void getWordsTest() throws Exception {
        //단어 조회 성공
        mockMvc.perform(get("/api/words/all")
                        .param("vocab_id", "1")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].wordId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].expression")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No vocab
        mockMvc.perform(get("/api/words/all")
                        .param("vocab_id", "150")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_VOCAB.getMessage())
                );

        //No param
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

    @DisplayName("[API][IT][GET] 단어 디테일 Test")
    @Test
    void getWordDetailTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/words/detail")
                        .param("word_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.expression")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No vocab
        mockMvc.perform(get("/api/words/detail")
                        .param("word_id", "150"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_WORD.getMessage())
                );

        //No param
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

    @DisplayName("[API][IT][POST] 단어 생성 Test")
    @Test
    @Transactional
    void createWordTest() throws Exception {
        CreateWord.Request request = CreateWord.Request.builder()
                .expression("TestWord")
                .build();
        //생성 성공
        mockMvc.perform(post("/api/words/{vocab_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.expression")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //Duplicated word
        mockMvc.perform(post("/api/words/{vocab_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_WORD.getMessage())
                );

        //No word
        mockMvc.perform(post("/api/words/{vocab_id}", 150)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_VOCAB.getMessage())
                );

        //Invalid request body
        request.setExpression(null);
        mockMvc.perform(post("/api/words/{vocab_id}", 1)
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
    
    @DisplayName("[API][IT][PATCH] 단어 수정 Test")
    @Test
    @Transactional
    void editWordTest() throws Exception {
        CreateWord.Request request = CreateWord.Request.builder()
                .expression("TestTitle")
                .build();
        //수정 성공
        mockMvc.perform(patch("/api/words/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.expression")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //Duplicated Title
        mockMvc.perform(patch("/api/words/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_WORD.getMessage())
                );

        //No user
        mockMvc.perform(patch("/api/words/{user_id}", 150)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_WORD.getMessage())
                );

        //Invalid request body
        request.setExpression(null);
        mockMvc.perform(patch("/api/words/{word_id}", 1)
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

    @DisplayName("[API][IT][DELETE] 단어 삭제 Test")
    @Test
    @Transactional
    void deleteWordTest() throws Exception {
        //삭제 성공
        mockMvc.perform(delete("/api/words/{word_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.expression")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //삭제 테스트 겸 No vocab
        mockMvc.perform(get("/api/words/detail")
                        .param("word_id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_WORD.getMessage())
                );
    }
}
