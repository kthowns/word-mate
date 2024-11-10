package com.example.myvoca.service;

import com.example.myvoca.dto.CreateTheme;
import com.example.myvoca.dto.EditTheme;
import com.example.myvoca.dto.ThemeDto;
import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.User;
import com.example.myvoca.exception.VocabException;
import com.example.myvoca.repository.ThemeRepository;
import com.example.myvoca.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.myvoca.exception.VocabErrorCode.NO_THEME;
import static com.example.myvoca.exception.VocabErrorCode.NO_USER;

@RequiredArgsConstructor
@Slf4j
@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public List<ThemeDto> getThemeByUserId(Integer userId) {
        return themeRepository.findByUser(getUserById(userId))
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
                .orElseThrow(() -> new VocabException(NO_USER));
    }

    private Theme getThemeById(Integer themeId){
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new VocabException(NO_THEME));
    }
}