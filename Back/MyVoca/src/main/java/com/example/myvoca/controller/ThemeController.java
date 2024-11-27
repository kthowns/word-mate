package com.example.myvoca.controller;

import com.example.myvoca.code.ApiResponseCode;
import com.example.myvoca.dto.ApiResponse;
import com.example.myvoca.dto.CreateTheme;
import com.example.myvoca.dto.ThemeDto;
import com.example.myvoca.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.myvoca.code.ApiResponseCode.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping("/themes/all")
    public ResponseEntity<?> getThemeByUserId(
            @Valid @RequestParam Integer user_id
    ){
        log.info("HTTP GET /api/themes/all?user_id="+user_id);
        return ApiResponse.toResponseEntity(OK,
                themeService.getThemes(user_id));
    }

    @GetMapping("/themes/detail")
    public ResponseEntity<?> getThemeByThemeId(
            @Valid @RequestParam Integer theme_id
    ){
        log.info("HTTP GET /api/themes/detail?detail_id="+theme_id);
        return ApiResponse.toResponseEntity(OK,
                themeService.getThemeDetail(theme_id));
    }

    @PostMapping("/themes/{user_id}")
    public ResponseEntity<?> createTheme(
            @Valid @PathVariable Integer user_id,
            @Valid @RequestBody CreateTheme.Request request
    ){
        log.info("HTTP POST /api/themes/"+user_id);
        return ApiResponse.toResponseEntity(OK,
                themeService.createTheme(user_id, request));
    }

    @PatchMapping("/themes/{theme_id}")
    public ResponseEntity<?> editTheme(
            @Valid @PathVariable Integer theme_id,
            @Valid @RequestBody CreateTheme.Request request
    ){
        log.info("HTTP PATCH /api/themes/"+theme_id);
        return ApiResponse.toResponseEntity(OK,
                themeService.editTheme(theme_id, request));
    }

    @DeleteMapping("/themes/{theme_id}")
    public ResponseEntity<?> deleteTheme(
            @Valid @PathVariable Integer theme_id
    ){
        log.info("HTTP DELETE /api/themes/="+theme_id);
        return ApiResponse.toResponseEntity(OK,
                themeService.deleteTheme(theme_id));
    }
}