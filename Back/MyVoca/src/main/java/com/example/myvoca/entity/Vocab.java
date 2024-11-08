package com.example.myvoca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vocabs")
public class Vocab {
    @Id
    @Column(name = "vocab_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vocabId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name="word_count")
    private Integer wordCount;
    @Column(name="created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}