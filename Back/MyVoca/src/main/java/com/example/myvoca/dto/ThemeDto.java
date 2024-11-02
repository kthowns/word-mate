package com.example.myvoca.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeDto {
    private String font;
    private Integer fontSize;
    private Integer color;
}
