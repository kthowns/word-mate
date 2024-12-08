package com.example.myvoca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "words")
public class Word {
    @Id
    @Column(name = "word_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wordId;
    @ManyToOne
    @JoinColumn(name = "vocab_id")
    private Vocab vocab;
    @Column(name = "expression")
    private String expression;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}