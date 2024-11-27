package com.example.myvoca.service;

import com.example.myvoca.dto.CreateTheme;
import com.example.myvoca.dto.ThemeDto;
import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.User;
import com.example.myvoca.repository.ThemeRepository;
import com.example.myvoca.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.myvoca.code.ApiResponseCode.NO_THEME;
import static com.example.myvoca.code.ApiResponseCode.NO_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ThemeService themeService;

    private final User defaultUser = User.builder()
            .userId(1)
            .username("user01")
            .password("pass01")
            .build();

    private final Theme defaultTheme = Theme.builder()
            .themeId(1)
            .user(defaultUser)
            .color("#151515")
            .font("굴림")
            .fontSize(12)
            .build();

    @DisplayName("[Service] 테마 가져오기 성공")
    @Test
    void getThemesTest_success() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.of(defaultUser));
        given(themeRepository.findByUser(defaultUser))
                .willReturn(Collections.singletonList(defaultTheme));
        //when
        List<ThemeDto> themes =
                themeService.getThemes(1);
        //then
        assertEquals(themes.get(0).getThemeId(), defaultTheme.getThemeId());
        assertEquals(themes.get(0).getColor(), defaultTheme.getColor());
        assertEquals(themes.get(0).getFont(), defaultTheme.getFont());
        assertEquals(themes.get(0).getFontSize(), defaultTheme.getFontSize());
    }

    @DisplayName("[Service] 테마 추가 성공")
    @Test
    void createThemeTest_success() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.of(defaultUser));
        given(themeRepository.save(any(Theme.class)))
                .willReturn(defaultTheme);
        ArgumentCaptor<Theme> captor =
                ArgumentCaptor.forClass(Theme.class);
        //when
        themeService.createTheme(
                1, toRequest(defaultTheme)
        );
        //then
        verify(themeRepository, times(1))
                .save(captor.capture());
        Theme savedTheme = captor.getValue();
        assertEquals(defaultTheme.getColor(), savedTheme.getColor());
        assertEquals(defaultTheme.getFont(), savedTheme.getFont());
        assertEquals(defaultTheme.getFontSize(), savedTheme.getFontSize());
    }
            
    @DisplayName("[Service] 테마 생성 NO_USER")
    @Test
    void createThemeTest_no_user() {
        //given
        given(userRepository.findById(1))
                .willReturn(Optional.empty());
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            themeService.createTheme(
                    1, toRequest(defaultTheme)
            );
        });
        //then
        assertEquals(e.getMessage(), NO_USER.getMessage());
    }

    @DisplayName("[Service] 테마 수정 NO_THEME")
    @Test
    void editThemeTest_no_theme() {
        //given
        given(themeRepository.findById(1))
                .willReturn(Optional.empty());
        CreateTheme.Request request = toRequest(defaultTheme);
        request.setFont("ZZZ");
        //when
        Throwable e = assertThrows(Exception.class, ()->{
            themeService.editTheme(1, request);
        });
        //then
        assertEquals(e.getMessage(), NO_THEME.getMessage());
    }
    
    @DisplayName("[Service] 테마 삭제 성공")
    @Test
    void deleteThemeTest_success() {
        //given
        given(themeRepository.findById(1))
                .willReturn(Optional.of(defaultTheme));
        //when
        ThemeDto deleteTheme = themeService.deleteTheme(1);
        //then
        assertEquals(defaultTheme.getThemeId(), deleteTheme.getThemeId());
        assertEquals(defaultTheme.getColor(), deleteTheme.getColor());
        assertEquals(defaultTheme.getFont(), deleteTheme.getFont());
        assertEquals(defaultTheme.getFontSize(), deleteTheme.getFontSize());
    }

    private CreateTheme.Request toRequest(Theme theme) {
        return CreateTheme.Request.builder()
                .color(theme.getColor())
                .font(theme.getFont())
                .fontSize(theme.getFontSize())
                .build();
    }
}