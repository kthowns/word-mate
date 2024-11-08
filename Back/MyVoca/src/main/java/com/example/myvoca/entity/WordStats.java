package com.example.myvoca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "word_stats")
public class WordStats {
    @Id
    private Integer wordId;

    @MapsId("wordId")
    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "incorrect_count")
    private Integer incorrectCount;

    @Column(name = "isLearned")
    private Integer isLearned;
}
