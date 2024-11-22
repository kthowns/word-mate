package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateDefinition;
import com.example.myvoca.dto.DefinitionDto;
import com.example.myvoca.entity.Definition;
import com.example.myvoca.entity.Word;
import com.example.myvoca.service.DefinitionService;
import com.example.myvoca.type.POS;
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

@WebMvcTest(DefinitionController.class)
class DefinitionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefinitionService definitionService;
    
    Word defaultWord = Word.builder()
            .wordId(1)
            .expression("expression")
            .build();

    Definition defaultDef = Definition.builder()
            .definitionId(1)
            .definition("definition")
            .type(POS.NOUN)
            .word(defaultWord)
            .build();

    @DisplayName("[API][GET] /api/defs/all 성공")
    @Test
    void getDefinitionsTest_success() throws Exception {
        //given
        List<DefinitionDto> response = Collections.singletonList(
                DefinitionDto.fromEntity(defaultDef));
        given(definitionService.getDefinitions(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/defs/all")
                        .param("word_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].definitionId")
                                .value(defaultDef.getDefinitionId())
                ).andExpect(
                        jsonPath("$.data[0].definition")
                                .value(defaultDef.getDefinition())
                ).andExpect(
                        jsonPath("$.data[0].type")
                                .value(defaultDef.getType().getType())
                );
    }

    @DisplayName("[API][GET] /api/defs/all No param")
    @Test
    void getDefinitionsTest_no_param() throws Exception {
        //given
        //when & then
        mockMvc.perform(get("/api/defs/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][POST] /api/defs/{user_id} 성공")
    @Test
    void createDefinitionTest_success() throws Exception {
        CreateDefinition.Request request = toRequest(defaultDef);
        CreateDefinition.Response response = CreateDefinition.Response
                        .fromEntity(defaultDef);
        //given
        given(definitionService.createDefinition(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/defs/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.definitionId")
                                .value(defaultDef.getDefinitionId())
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(defaultDef.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(defaultDef.getType().getType())
                );
    }

    @DisplayName("[API][POST] /api/defs/{vocab_id} Invalid request")
    @Test
    void createDefinitionTest_invalid_request() throws Exception {
        CreateDefinition.Request request = toRequest(defaultDef);
        CreateDefinition.Response response = CreateDefinition.Response
                .fromEntity(defaultDef);
        request.setDefinition(null);
        //given
        given(definitionService.createDefinition(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/defs/{vocab_id}", 1)
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

    @DisplayName("[API][PATCH] /api/defs/{definition_id} 성공")
    @Test
    void editDefinitionTest_success() throws Exception {
        CreateDefinition.Request request = toRequest(defaultDef);
        request.setDefinition("Hello");
        CreateDefinition.Response response = CreateDefinition.Response.fromEntity(defaultDef);
        response.setDefinition(request.getDefinition());
        //given
        given(definitionService.editDefinition(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/defs/{definition_id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.definitionId")
                                .value(defaultDef.getDefinitionId())
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(request.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(defaultDef.getType().getType())
                );
    }

    @DisplayName("[API][DELETE] /api/defs/{definition_id} 성공")
    @Test
    void deleteDefinitionTest_success() throws Exception {
        //given
        given(definitionService.deleteDefinition(1))
                .willReturn(DefinitionDto.fromEntity(defaultDef));
        //when & then
        mockMvc.perform(delete("/api/defs/{definition_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.definitionId")
                                .value(defaultDef.getDefinitionId())
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(defaultDef.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(defaultDef.getType().getType())
                );
    }

    private CreateDefinition.Request toRequest(Definition definition) {
        return CreateDefinition.Request.builder()
                .definition(definition.getDefinition())
                .type(definition.getType())
                .build();
    }
}