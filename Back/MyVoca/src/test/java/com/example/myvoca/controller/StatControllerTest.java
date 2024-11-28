package com.example.myvoca.controller;

import com.example.myvoca.dto.StatDto;
import com.example.myvoca.dto.UpdateStat;
import com.example.myvoca.entity.Stat;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import com.example.myvoca.service.StatService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatController.class)
class StatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatService statService;

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

    Stat defaultStat = Stat.builder()
            .word(defaultWord)
            .correctCount(6)
            .incorrectCount(3)
            .isLearned(1)
            .build();

    @DisplayName("[API][GET] /api/stats/all 성공")
    @Test
    void getStatsTest_success() throws Exception {
        //given
        List<StatDto> response = Collections.singletonList(
                StatDto.fromEntity(defaultStat));
        given(statService.getStats(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/stats/all")
                        .param("vocab_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].wordId")
                                .value(defaultStat.getWord().getWordId())
                ).andExpect(
                        jsonPath("$.data[0].correctCount")
                                .value(defaultStat.getCorrectCount())
                ).andExpect(
                        jsonPath("$.data[0].incorrectCount")
                                .value(defaultStat.getIncorrectCount())
                ).andExpect(
                        jsonPath("$.data[0].isLearned")
                                .value(defaultStat.getIsLearned())
                );
    }

    @DisplayName("[API][GET] /api/stats/all No param")
    @Test
    void getStatsTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][GET] /api/stats/detail 성공")
    @Test
    void getStatsDetailTest_success() throws Exception {
        //given
        StatDto response = StatDto.fromEntity(defaultStat);
        given(statService.getStatDetail(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/stats/detail")
                        .param("word_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultStat.getWord().getWordId())
                ).andExpect(
                        jsonPath("$.data.correctCount")
                                .value(defaultStat.getCorrectCount())
                ).andExpect(
                        jsonPath("$.data.incorrectCount")
                                .value(defaultStat.getIncorrectCount())
                ).andExpect(
                        jsonPath("$.data.isLearned")
                                .value(defaultStat.getIsLearned())
                );
    }

    @DisplayName("[API][GET] /api/stats/detail No param")
    @Test
    void getStatsDetailTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][PATCH] /api/stats/{stats_id} 성공")
    @Test
    void updateStatsTest_success() throws Exception {
        UpdateStat.Request request = toRequest(defaultStat);
        request.setCorrectCount(5);
        UpdateStat.Response response = UpdateStat.Response.fromEntity(defaultStat);
        response.setCorrectCount(request.getCorrectCount());
        //given
        given(statService.updateStat(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/stats/{stats_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.wordId")
                                .value(defaultStat.getWord().getWordId())
                ).andExpect(
                        jsonPath("$.data.correctCount")
                                .value(request.getCorrectCount())
                ).andExpect(
                        jsonPath("$.data.incorrectCount")
                                .value(defaultStat.getIncorrectCount())
                ).andExpect(
                        jsonPath("$.data.isLearned")
                                .value(defaultStat.getIsLearned())
                );
    }

    @DisplayName("[API][PATCH] /api/stats/{stats_id} Invalid request")
    @Test
    void updateStatsTest_invalid_request() throws Exception {
        UpdateStat.Request request = toRequest(defaultStat);
        UpdateStat.Response response = UpdateStat.Response
                .fromEntity(defaultStat);
        request.setCorrectCount(-1);
        //given
        given(statService.updateStat(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/stats/{stats_id}", 1)
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

    @DisplayName("[API][GET] /api/stats/lr 성공")
    @Test
    void getLearningRateTest_success() throws Exception {
        //given
        given(statService.getLearningRate(1))
                .willReturn(0.5);
        //when & then
        mockMvc.perform(get("/api/stats/lr")
                        .param("vocab_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data")
                                .value(0.5)
                );
    }

    @DisplayName("[API][GET] /api/stats/diff 성공")
    @Test
    void getDifficultyTest_success() throws Exception {
        //given
        given(statService.getDifficulty(1))
                .willReturn(0.5);
        //when & then
        mockMvc.perform(get("/api/stats/diff")
                        .param("word_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data")
                                .value(0.5)
                );
    }

    private UpdateStat.Request toRequest(Stat stat) {
        return UpdateStat.Request.builder()
                .correctCount(stat.getCorrectCount())
                .incorrectCount(stat.getIncorrectCount())
                .isLearned(stat.getIsLearned())
                .build();
    }
}