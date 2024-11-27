package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateVocab;
import com.example.myvoca.dto.VocabDto;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.service.VocabService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.example.myvoca.code.ApiResponseCode.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VocabController.class)
class VocabControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VocabService vocabService;

    User defaultUser = User.builder()
            .userId(1)
            .username("user01")
            .password("pass01")
            .build();

    Vocab defaultVocab = Vocab.builder()
            .vocabId(1)
            .user(defaultUser)
            .title("title")
            .description("description that over 15 letters")
            .wordCount(1)
            .build();

    @DisplayName("[API][GET] /api/vocabs/all 성공")
    @Test
    void getVocabsTest_success() throws Exception {
        //given
        List<VocabDto> response = Collections.singletonList(
                VocabDto.fromEntity(defaultVocab));
        given(vocabService.getVocabs(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/vocabs/all")
                        .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].vocabId")
                                .value(defaultVocab.getVocabId())
                ).andExpect(
                        jsonPath("$.data[0].title")
                                .value(defaultVocab.getTitle())
                ).andExpect(
                        jsonPath("$.data[0].description")
                                .value(defaultVocab.getDescription())
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );
    }

    @DisplayName("[API][GET] /api/vocabs/all No param")
    @Test
    void getVocabsTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][GET] /api/vocabs/detail 성공")
    @Test
    void getVocabDetailTest_success() throws Exception {
        //given
        VocabDto response = VocabDto.fromEntity(defaultVocab);
        given(vocabService.getVocabDetail(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/vocabs/detail")
                        .param("vocab_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .value(defaultVocab.getVocabId())
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(defaultVocab.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(defaultVocab.getDescription())
                );
    }

    @DisplayName("[API][GET] /api/vocabs/detail No param")
    @Test
    void getVocabDetailTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][POST] /api/vocabs/{user_id} 성공")
    @Test
    void createVocabTest_success() throws Exception {
        CreateVocab.Request request = toRequest(defaultVocab);
        CreateVocab.Response response = CreateVocab.Response
                        .fromEntity(defaultVocab);
        //given
        given(vocabService.createVocab(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/vocabs/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .value(defaultVocab.getVocabId())
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(defaultVocab.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(defaultVocab.getDescription()
                                        .substring(0, 15) + "...")
                );
    }

    @DisplayName("[API][POST] /api/vocabs/{user_id} Invalid request")
    @Test
    void createVocabTest_invalid_request() throws Exception {
        CreateVocab.Request request = toRequest(defaultVocab);
        CreateVocab.Response response = CreateVocab.Response
                .fromEntity(defaultVocab);
        request.setTitle(null);
        //given
        given(vocabService.createVocab(1, request))
                .willReturn(response);
        //when & then
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

    @DisplayName("[API][PATCH] /api/vocabs/{vocab_id} 성공")
    @Test
    void editVocabTest_success() throws Exception {
        CreateVocab.Request request = toRequest(defaultVocab);
        request.setTitle("Hello");
        request.setDescription("This is Description");
        CreateVocab.Response response = CreateVocab.Response.fromEntity(defaultVocab);
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription().substring(0, 15) + "...");
        //given
        given(vocabService.editVocab(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/vocabs/{vocab_id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .value(defaultVocab.getVocabId())
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(request.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(request.getDescription()
                                        .substring(0, 15) + "...")
                );
    }

    @DisplayName("[API][DELETE] /api/vocabs/{vocab_id} 성공")
    @Test
    void deleteVocabTest_success() throws Exception {
        //given
        given(vocabService.deleteVocab(1))
                .willReturn(VocabDto.fromEntity(defaultVocab));
        //when & then
        mockMvc.perform(delete("/api/vocabs/{vocab_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.vocabId")
                                .value(defaultVocab.getVocabId())
                ).andExpect(
                        jsonPath("$.data.title")
                                .value(defaultVocab.getTitle())
                ).andExpect(
                        jsonPath("$.data.description")
                                .value(defaultVocab.getDescription())
                );
    }

    private CreateVocab.Request toRequest(Vocab vocab) {
        return CreateVocab.Request.builder()
                .description(vocab.getDescription())
                .title(vocab.getTitle())
                .build();
    }
}