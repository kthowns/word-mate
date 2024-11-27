package com.example.myvoca.controller;

import com.example.myvoca.dto.CreateTheme;
import com.example.myvoca.dto.ThemeDto;
import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.User;
import com.example.myvoca.service.ThemeService;
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

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ThemeService themeService;

    User defaultUser = User.builder()
            .userId(1)
            .username("user01")
            .password("pass01")
            .build();

    Theme defaultTheme = Theme.builder()
            .themeId(1)
            .user(defaultUser)
            .font("font1")
            .fontSize(12)
            .color("#151515")
            .build();

    @DisplayName("[API][GET] /api/themes/all 성공")
    @Test
    void getThemesTest_success() throws Exception {
        //given
        List<ThemeDto> response = Collections.singletonList(
                ThemeDto.fromEntity(defaultTheme));
        given(themeService.getThemes(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/themes/all")
                        .param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data[0].themeId")
                                .value(defaultTheme.getThemeId())
                ).andExpect(
                        jsonPath("$.data[0].font")
                                .value(defaultTheme.getFont())
                ).andExpect(
                        jsonPath("$.data[0].color")
                                .value(defaultTheme.getColor())
                );
    }

    @DisplayName("[API][GET] /api/themes/all No param")
    @Test
    void getThemesTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][GET] /api/themes/detail 성공")
    @Test
    void getThemeDetailTest_success() throws Exception {
        //given
        ThemeDto response = ThemeDto.fromEntity(defaultTheme);
        given(themeService.getThemeDetail(1))
                .willReturn(response);
        //when & then
        mockMvc.perform(get("/api/themes/detail")
                        .param("theme_id", "1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .value(defaultTheme.getThemeId())
                ).andExpect(
                        jsonPath("$.data.font")
                                .value(defaultTheme.getFont())
                ).andExpect(
                        jsonPath("$.data.color")
                                .value(defaultTheme.getColor())
                );
    }

    @DisplayName("[API][GET] /api/themes/detail No param")
    @Test
    void getThemeDetailTest_no_param() throws Exception {
        //given
        //when & then
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

    @DisplayName("[API][POST] /api/themes/{user_id} 성공")
    @Test
    void createThemeTest_success() throws Exception {
        CreateTheme.Request request = toRequest(defaultTheme);
        CreateTheme.Response response = CreateTheme.Response
                .fromEntity(defaultTheme);
        //given
        given(themeService.createTheme(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(post("/api/themes/{user_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .value(defaultTheme.getThemeId())
                ).andExpect(
                        jsonPath("$.data.font")
                                .value(defaultTheme.getFont())
                ).andExpect(
                        jsonPath("$.data.color")
                                .value(defaultTheme.getColor())
                );
    }

    @DisplayName("[API][POST] /api/themes/{user_id} Invalid request")
    @Test
    void createThemeTest_invalid_request() throws Exception {
        CreateTheme.Request request = toRequest(defaultTheme);
        CreateTheme.Response response = CreateTheme.Response
                .fromEntity(defaultTheme);
        request.setFont(null);
        //given
        given(themeService.createTheme(1, request))
                .willReturn(response);
        //when & then
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

    @DisplayName("[API][POST] /api/themes/{user_id} Invalid color")
    @Test
    void createThemeTest_invalid_color() throws Exception {
        CreateTheme.Request request = toRequest(defaultTheme);
        CreateTheme.Response response = CreateTheme.Response
                .fromEntity(defaultTheme);
        request.setColor("#13131");
        //given
        given(themeService.createTheme(1, request))
                .willReturn(response);
        //when & then
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

    @DisplayName("[API][PATCH] /api/themes/{theme_id} 성공")
    @Test
    void editThemeTest_success() throws Exception {
        CreateTheme.Request request = toRequest(defaultTheme);
        request.setFont("Hello");
        request.setColor("#232323");
        CreateTheme.Response response = CreateTheme.Response.fromEntity(defaultTheme);
        response.setFont(request.getFont());
        response.setColor(request.getColor());
        //given
        given(themeService.editTheme(1, request))
                .willReturn(response);
        //when & then
        mockMvc.perform(patch("/api/themes/{theme_id}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .value(defaultTheme.getThemeId())
                ).andExpect(
                        jsonPath("$.data.font")
                                .value(request.getFont())
                ).andExpect(
                        jsonPath("$.data.color")
                                .value(request.getColor())
                );
    }

    @DisplayName("[API][DELETE] /api/themes/{theme_id} 성공")
    @Test
    void deleteThemeTest_success() throws Exception {
        //given
        given(themeService.deleteTheme(1))
                .willReturn(ThemeDto.fromEntity(defaultTheme));
        //when & then
        mockMvc.perform(delete("/api/themes/{theme_id}", 1)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.status")
                                .value(200)
                ).andExpect(
                        jsonPath("$.data.themeId")
                                .value(defaultTheme.getThemeId())
                ).andExpect(
                        jsonPath("$.data.font")
                                .value(defaultTheme.getFont())
                ).andExpect(
                        jsonPath("$.data.color")
                                .value(defaultTheme.getColor())
                );
    }

    private CreateTheme.Request toRequest(Theme theme) {
        return CreateTheme.Request.builder()
                .font(theme.getFont())
                .fontSize(theme.getFontSize())
                .color(theme.getColor())
                .build();
    }
}