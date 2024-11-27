package com.example.myvoca.dto;

import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.Vocab;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeDto {
    private Integer themeId;
    private String font;
    private Integer fontSize;
    private String color;
    public static ThemeDto fromEntity(Theme theme) {
        return ThemeDto.builder()
                .font(theme.getFont())
                .fontSize(theme.getFontSize())
                .color(theme.getColor())
                .themeId(theme.getThemeId())
                .build();
    }
}
