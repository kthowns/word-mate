package com.example.myvoca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vocab_word")
public class VocabWord{
    @EmbeddedId
    private VocabWordId vocabWordId;

    @ManyToOne
    @MapsId("vocabId")
    @JoinColumn(name="vocab_id")
    private Vocab vocab;

    @ManyToOne
    @MapsId("wordId")
    @JoinColumn(name="word_id")
    private Word word;
}
