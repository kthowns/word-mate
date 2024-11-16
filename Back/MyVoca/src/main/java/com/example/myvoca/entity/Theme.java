package com.example.myvoca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Integer themeId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "font")
    private String font;
    @Column(name = "font_size")
    private Integer fontSize;
    @Column(name = "color")
    private String color;
}
