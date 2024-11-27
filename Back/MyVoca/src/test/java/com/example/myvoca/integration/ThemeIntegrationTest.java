package com.example.myvoca.integration;

import com.example.myvoca.dto.CreateTheme;
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
public class ThemeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("[API][IT][GET] 테마 조회 Test")
    @Test
    void getThemesTest() throws Exception {
        //테마 조회 성공
        mockMvc.perform(get("/api/themes/all")
                        .param("user_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].themeId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].font")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].fontSize")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data[0].color")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No user
        mockMvc.perform(get("/api/themes/all")
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
        mockMvc.perform(get("/api/themes/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][GET] 테마 디테일 Test")
    @Test
    void getThemeDetailTest() throws Exception {
        //성공
        mockMvc.perform(get("/api/themes/detail")
                        .param("theme_id", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.font")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.fontSize")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.color")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No theme
        mockMvc.perform(get("/api/themes/detail")
                        .param("theme_id", "150"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_THEME.getMessage())
                );

        //No param
        mockMvc.perform(get("/api/themes/detail"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.status")
                                .value(500)
                ).andExpect(
                        jsonPath("$.message")
                                .value(INTERNAL_SERVER_ERROR.getMessage())
                );
    }

    @DisplayName("[API][IT][POST] 테마 생성 Test")
    @Test
    @Transactional
    void createThemeTest() throws Exception {
        CreateTheme.Request request = CreateTheme.Request.builder()
                .font("TestFont")
                .fontSize(20)
                .color("#151515")
                .build();
        //생성 성공
        mockMvc.perform(post("/api/themes/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.font")
                                .value("TestFont")
                ).andExpect(
                        jsonPath("$.data.fontSize")
                                .value(20)
                ).andExpect(
                        jsonPath("$.data.color")
                                .value("#151515")
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No user
        mockMvc.perform(post("/api/themes/{user_id}", 150)
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
        request.setColor("#11");
        mockMvc.perform(post("/api/themes/{user_id}", 1)
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

    @DisplayName("[API][IT][PATCH] 테마 수정 Test")
    @Test
    @Transactional
    void editThemeTest() throws Exception {
        CreateTheme.Request request = CreateTheme.Request.builder()
                .font("TestFont")
                .fontSize(20)
                .color("#151515")
                .build();
        //수정 성공
        mockMvc.perform(patch("/api/themes/{theme_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.font")
                                .value("TestFont")
                ).andExpect(
                        jsonPath("$.data.fontSize")
                                .value(20)
                ).andExpect(
                        jsonPath("$.data.color")
                                .value("#151515")
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //No theme
        mockMvc.perform(patch("/api/themes/{user_id}", 150)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_THEME.getMessage())
                );

        //Invalid request body
        request.setColor("#1512");
        mockMvc.perform(patch("/api/themes/{user_id}", 1)
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

    @DisplayName("[API][IT][DELETE] 테마 삭제 Test")
    @Test
    @Transactional
    void deleteThemeTest() throws Exception {
        //삭제 성공
        mockMvc.perform(delete("/api/themes/{theme_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.font")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.fontSize")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.data.color")
                                .isNotEmpty()
                ).andExpect(
                        jsonPath("$.message")
                                .value(OK.getMessage())
                );

        //삭제 테스트 겸 No theme
        mockMvc.perform(get("/api/themes/detail")
                .param("theme_id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value(NO_THEME.getMessage())
                );
    }
}