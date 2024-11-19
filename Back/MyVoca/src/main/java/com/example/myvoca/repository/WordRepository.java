package com.example.myvoca.repository;

import com.example.myvoca.entity.Vocab;
import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepository
    extends JpaRepository<Word, Integer> {
    @Query("SELECT w FROM Word w where w.vocab.vocabId = :vocabId")
    List<Word> findWordsByVocabId(@Param("vocabId") Integer vocabId);

    Optional<Word> findByExpressionAndVocab(String expression, Vocab vocabById);
}
