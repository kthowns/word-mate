package com.example.myvoca.entity;

import com.example.myvoca.type.POS;
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
@Table(name="definition")
public class Definition {
    @Id
    @Column(name="definition_id")
    private Integer definitionId;
    @Column(name="definition")
    private String definition;
    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private POS type;
}
