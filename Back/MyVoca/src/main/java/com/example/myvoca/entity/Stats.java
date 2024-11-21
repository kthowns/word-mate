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
@Table(name = "stats")
public class Stats {
    @Id
    private Integer wordId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "incorrect_count")
    private Integer incorrectCount;

    @Column(name = "is_learned")
    private Integer isLearned;
}
