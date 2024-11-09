package com.example.myvoca.controller;

import com.example.myvoca.dto.*;
import com.example.myvoca.service.ThemeService;
import com.example.myvoca.service.VocabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping("/themes/all")
    public List<ThemeDto> getThemeByUserId(
            @Valid @RequestParam Integer user_id
    ){
        log.info("HTTP GET /api/themes/all?user_id="+user_id);
        return themeService.getThemeByUserId(user_id);
    }

    @GetMapping("/themes/detail")
    public ThemeDto getThemeByThemeId(
            @Valid @RequestParam Integer theme_id
    ){
        log.info("HTTP GET /api/themes/detail?detail_id="+theme_id);
        return themeService.getThemeDtoById(theme_id);
    }

    @PostMapping("/themes/{user_id}")
    public CreateTheme.Response createTheme(
            @Valid @PathVariable Integer user_id,
            @Valid @RequestBody CreateTheme.Request request
    ){
        log.info("HTTP POST /api/themes/"+user_id);
        return themeService.createTheme(user_id, request);
    }

    @PatchMapping("/themes/{theme_id}")
    public ThemeDto editTheme(
            @Valid @PathVariable Integer theme_id,
            @Valid @RequestBody EditTheme.Request request
    ){
        log.info("HTTP PATCH /api/themes/"+theme_id);
        return themeService.editTheme(theme_id, request);
    }

    @DeleteMapping("/themes/{theme_id}")
    public ThemeDto deleteTheme(
            @Valid @PathVariable Integer theme_id
    ){
        log.info("HTTP DELETE /api/themes/="+theme_id);
        return themeService.deleteTheme(theme_id);
    }
}