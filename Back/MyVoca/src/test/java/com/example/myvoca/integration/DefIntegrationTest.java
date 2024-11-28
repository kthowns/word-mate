package com.example.myvoca.integration;

import com.example.myvoca.dto.CreateDef;
import com.example.myvoca.type.POS;
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
public class DefIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][GET] 뜻 조회 Test")
    @Test
    void getDefinitionsTest() throws Exception {
        //뜻 조회 성공
        mockMvc.perform(get("/api/defs/all")
                        .param("word_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].defId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].definition")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].type")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No word
        mockMvc.perform(get("/api/defs/all")
                        .param("word_id", "150")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_WORD.getMessage())
                );

        //No param
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

    @DisplayName("[API][IT][POST] 뜻 생성 Test")
    @Test
    @Transactional
    void createDefinitionTest() throws Exception {
        CreateDef.Request request = CreateDef.Request.builder()
                .definition("TestDefinition")
                .type(POS.NOUN)
                .build();
        //생성 성공
        mockMvc.perform(post("/api/defs/{word_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.defId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(request.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(request.getType().getType())
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //Duplicated definition
        mockMvc.perform(post("/api/defs/{word_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_DEFINITION.getMessage())
                );

        //No word
        mockMvc.perform(post("/api/defs/{word_id}", 150)
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
        request.setDefinition(null);
        mockMvc.perform(post("/api/defs/{word_id}", 1)
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

    @DisplayName("[API][IT][PATCH] 뜻 수정 Test")
    @Test
    @Transactional
    void editDefinitionTest() throws Exception {
        CreateDef.Request request = CreateDef.Request.builder()
                .definition("TestDefinition")
                .type(POS.NOUN)
                .build();

        //수정 성공
        mockMvc.perform(patch("/api/defs/{def_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.defId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.definition")
                                .value(request.getDefinition())
                ).andExpect(
                        jsonPath("$.data.type")
                                .value(request.getType().getType())
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //Duplicated definition
        mockMvc.perform(patch("/api/defs/{def_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                ).andExpect(
                        jsonPath("$.message")
                                .value(DUPLICATED_DEFINITION.getMessage())
                );

        //No def
        mockMvc.perform(patch("/api/defs/{def_id}", 150)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_DEF.getMessage())
                );

        //Invalid request body
        request.setDefinition(null);
        mockMvc.perform(patch("/api/defs/{def_id}", 1)
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

    @DisplayName("[API][IT][DELETE] 뜻 삭제 Test")
    @Test
    @Transactional
    void deleteDefinitionTest() throws Exception {
        //삭제 성공
        mockMvc.perform(delete("/api/defs/{def_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.defId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.definition")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.type")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );
    }
}