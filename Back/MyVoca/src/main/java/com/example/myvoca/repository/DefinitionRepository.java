package com.example.myvoca.repository;

import com.example.myvoca.entity.Definition;
import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DefinitionRepository
    extends JpaRepository<Definition, Integer> {
    List<Definition> findByWord(Word word);
}