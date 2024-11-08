package com.example.myvoca.service;

import com.example.myvoca.dto.*;
import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import com.example.myvoca.repository.ThemeRepository;
import com.example.myvoca.repository.UserRepository;
import com.example.myvoca.repository.VocabRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public List<ThemeDto> getThemeByUserId(Integer userId) {
        return themeRepository.findByUserId(userId)
                .stream().map(ThemeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ThemeDto getThemeDtoById(Integer themeId) {
        return ThemeDto.fromEntity(getThemeById(themeId));
    }

    @Transactional
    public CreateTheme.Response createTheme(Integer userId, CreateTheme.Request request) {
        Theme theme = Theme.builder()
                .user(getUserById(userId))
                .font(request.getFont())
                .fontSize(request.getFontSize())
                .color(request.getColor())
                .build();
        theme = themeRepository.save(theme);
        return CreateTheme.Response.fromEntity(theme);
    }

    @Transactional
    public ThemeDto editTheme(Integer themeId, EditTheme.Request request) {
        Theme theme = getThemeById(themeId);
        theme.setFont(request.getFont());
        theme.setFontSize(request.getFontSize());
        theme.setColor(request.getColor());

        return ThemeDto.fromEntity(theme);
    }

    @Transactional
    public ThemeDto deleteTheme(Integer themeId) {
        Theme theme = getThemeById(themeId);
        themeRepository.delete(theme);

        return ThemeDto.fromEntity(theme);
    }

    private User getUserById(Integer userId){
        return userRepository.findById(userId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Theme getThemeById(Integer themeId){
        return themeRepository.findById(themeId)
                .orElseThrow(NoSuchElementException::new);
    }
}