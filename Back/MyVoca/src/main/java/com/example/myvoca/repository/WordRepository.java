package com.example.myvoca.repository;

import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepository
    extends JpaRepository<Word, Integer> {
    @Query("SELECT w FROM Word w WHERE w.wordId IN (" +
            "SELECT vw.word.wordId FROM VocabWord vw WHERE vw.vocab.vocabId = :vocabId)")
    List<Word> findWordByVocabId(@Param("vocabId") Integer vocabId);
}
