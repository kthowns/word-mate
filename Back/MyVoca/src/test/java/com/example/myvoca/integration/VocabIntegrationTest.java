package com.example.myvoca.integration;

import com.example.myvoca.dto.CreateVocab;
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
public class VocabIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][GET] 단어장 조회 Test")
    @Test
    void getVocabsTest() throws Exception {
        //단어장 조회 성공
        mockMvc.perform(get("/api/vocabs/all")
                        .param("user_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].vocabId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].title")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].description")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No user
        mockMvc.perform(get("/api/vocabs/all")
                        .param("user_id", "150")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_USER.getMessage())
                );

        //No param
        mockMvc.perform(get("/api/vocabs/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][GET] 단어장 디테일 Test")
    @Test
    void getVocabDetailTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/vocabs/detail")
                        .param("vocab_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.title")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.description")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No vocab
        mockMvc.perform(get("/api/vocabs/detail")
                        .param("vocab_id", "150"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_VOCAB.getMessage())
                );

        //No param
        mockMvc.perform(get("/api/vocabs/detail"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][POST] 단어장 생성 Test")
    @Test
    @Transactional
    void createVocabTest() throws Exception {
        CreateVocab.Request request = CreateVocab.Request.builder()
                .title("TestTitle")
                .description("TestDescriptions")
                .build();
        //생성 성공
        mockMvc.perform(post("/api/vocabs/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(request.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(request.getDescription()
                                        .substring(0, 15) + "...")
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //Duplicated Title
        mockMvc.perform(post("/api/vocabs/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_TITLE.getMessage())
                );

        //No user
        mockMvc.perform(post("/api/vocabs/{user_id}", 150)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_USER.getMessage())
                );

        //Invalid request body
        request.setTitle(null);
        mockMvc.perform(post("/api/vocabs/{user_id}", 1)
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

    @DisplayName("[API][IT][PATCH] 단어장 수정 Test")
    @Test
    @Transactional
    void editVocabTest() throws Exception {
        CreateVocab.Request request = CreateVocab.Request.builder()
                .title("TestTitle")
                .description("TestDescriptions")
                .build();
        //수정 성공
        mockMvc.perform(patch("/api/vocabs/{vocab_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(request.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(request.getDescription()
                                        .substring(0, 15) + "...")
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );


        //Duplicated Title
        mockMvc.perform(patch("/api/vocabs/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_TITLE.getMessage())
                );

        //No user
        mockMvc.perform(patch("/api/vocabs/{user_id}", 150)
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
        request.setTitle(null);
        mockMvc.perform(patch("/api/vocabs/{user_id}", 1)
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

    @DisplayName("[API][IT][DELETE] 단어장 삭제 Test")
    @Test
    @Transactional
    void deleteVocabTest() throws Exception {
        //삭제 성공
        mockMvc.perform(delete("/api/vocabs/{vocab_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.title")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.description")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //삭제 테스트 겸 No vocab
        mockMvc.perform(get("/api/vocabs/detail")
                .param("vocab_id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_VOCAB.getMessage())
                );
    }
}