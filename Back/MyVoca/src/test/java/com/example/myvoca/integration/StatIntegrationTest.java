package com.example.myvoca.integration;

import com.example.myvoca.dto.UpdateStat;
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
public class StatIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][GET] 통계 조회 Test")
    @Test
    void getStatsTest() throws Exception {
        //통계 조회 성공
        mockMvc.perform(get("/api/stats/all")
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
                        jsonPath("$.data[0].correctCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].incorrectCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].isLearned")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No vocab
        mockMvc.perform(get("/api/stats/all")
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
        mockMvc.perform(get("/api/stats/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][GET] 통계 디테일 Test")
    @Test
    void getStatDetailTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/stats/detail")
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
                        jsonPath("$.data.correctCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.incorrectCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.isLearned")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No word
        mockMvc.perform(get("/api/stats/detail")
                        .param("word_id", "150"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_STATS.getMessage())
                );

        //No param
        mockMvc.perform(get("/api/stats/detail"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][PATCH] 통계 업데이트 Test")
    @Test
    @Transactional
    void updateStatTest() throws Exception {
        UpdateStat.Request request = UpdateStat.Request.builder()
                .correctCount(7)
                .incorrectCount(8)
                .isLearned(1)
                .build();

        //업데이트 성공
        mockMvc.perform(patch("/api/stats/{word_id}", 1)
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
                        jsonPath("$.data.correctCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.incorrectCount")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.isLearned")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No word
        mockMvc.perform(patch("/api/stats/{word_id}", 150)
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
    }

    @DisplayName("[API][IT][GET] 통계 Learning Rate Test")
    @Test
    void getLearningRateTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/stats/lr")
                        .param("vocab_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data")
                                .isNumber()
                );

        //No vocab
        mockMvc.perform(get("/api/stats/lr")
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
        mockMvc.perform(get("/api/stats/lr"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][GET] 통계 Difficulty Test")
    @Test
    void getDifficultyTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/stats/diff")
                        .param("word_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data")
                                .isNumber()
                );

        //No stats
        mockMvc.perform(get("/api/stats/diff")
                        .param("word_id", "150"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_STATS.getMessage())
                );

        //No param
        mockMvc.perform(get("/api/stats/diff"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }
}