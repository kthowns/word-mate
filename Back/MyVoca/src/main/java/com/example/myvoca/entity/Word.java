package com.example.myvoca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "word")
public class Word {
    @Id
    private Integer wordId;
    @Column(name = "expression")
    private String expression;
    @Column(name = "created_at")
    private Timestamp createdAt;
}