package com.example.myvoca.entity;

import com.example.myvoca.type.POS;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="definition")
@EqualsAndHashCode
public class Definition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="definition_id")
    private Integer definitionId;
    @ManyToOne
    @JoinColumn(name="word_id")
    private Word word;
    @Column(name="definition")
    private String definition;
    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private POS type;
}
