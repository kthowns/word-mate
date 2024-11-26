package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateDef;
import com.example.myvoca.dto.DefDto;
import com.example.myvoca.entity.Def;
import com.example.myvoca.entity.Word;
import com.example.myvoca.service.DefService;
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

@WebMvcTest(DefController.class)
class DefControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefService defService;
    
    Word defaultWord = Word.builder()
            .wordId(1)
            .expression("expression")
            .build();

    Def defaultDef = Def.builder()
            .defId(1)
            .definition("definition")
            .type(POS.NOUN)
            .word(defaultWord)
            .build();

    @DisplayName("[API][GET] /api/defs/all 성공")
    @Test
    void getDefTest_success() throws Exception {
        //given
        List<DefDto> response = Collections.singletonList(
                DefDto.fromEntity(defaultDef));
        given(defService.getDefs(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/defs/all")
                        .param("word_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].defId")
                                .value(defaultDef.getDefId())
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
    void getDefTest_no_param() throws Exception {
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
    void createDefTest_success() throws Exception {
        CreateDef.Request request = toRequest(defaultDef);
        CreateDef.Response response = CreateDef.Response
                        .fromEntity(defaultDef);
        //given
        given(defService.createDef(1, request))
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
                        jsonPath("$.data.defId")
                                .value(defaultDef.getDefId())
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
        CreateDef.Request request = toRequest(defaultDef);
        CreateDef.Response response = CreateDef.Response
                .fromEntity(defaultDef);
        request.setDefinition(null);
        //given
        given(defService.createDef(1, request))
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

    @DisplayName("[API][PATCH] /api/defs/{def_id} 성공")
    @Test
    void editDefTest_success() throws Exception {
        CreateDef.Request request = toRequest(defaultDef);
        request.setDefinition("Hello");
        CreateDef.Response response = CreateDef.Response.fromEntity(defaultDef);
        response.setDefinition(request.getDefinition());
        //given
        given(defService.editDef(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/defs/{def_id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.defId")
                                .value(defaultDef.getDefId())
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(request.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(defaultDef.getType().getType())
                );
    }

    @DisplayName("[API][DELETE] /api/defs/{def_id} 성공")
    @Test
    void deleteDefTest_success() throws Exception {
        //given
        given(defService.deleteDef(1))
                .willReturn(DefDto.fromEntity(defaultDef));
        //when & then
        mockMvc.perform(delete("/api/defs/{def_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.defId")
                                .value(defaultDef.getDefId())
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(defaultDef.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(defaultDef.getType().getType())
                );
    }

    private CreateDef.Request toRequest(Def def) {
        return CreateDef.Request.builder()
                .definition(def.getDefinition())
                .type(def.getType())
                .build();
    }
}