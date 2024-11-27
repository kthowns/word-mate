package com.example.myvoca.repository;

import com.example.myvoca.entity.Def;
import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DefRepository
    extends JpaRepository<Def, Integer> {
    List<Def> findByWord(Word word);

    Optional<Def> findByDefinitionAndWord(String definition, Word word);
}