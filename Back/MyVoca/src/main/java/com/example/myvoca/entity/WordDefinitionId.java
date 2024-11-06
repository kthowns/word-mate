package com.example.myvoca.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class WordDefinitionId implements Serializable {
    private Integer word_id;
    private Integer definition_id;
}
