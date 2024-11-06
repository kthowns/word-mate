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
@Table(name = "word_definition")
public class WordDefinition {
    @EmbeddedId
    private WordDefinitionId wordDefinitionId;

    @ManyToOne
    @MapsId("word_id")
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @MapsId("definition_id")
    @JoinColumn(name = "definition_id")
    private Definition definition;
}