package com.example.myvoca.repository;

import com.example.myvoca.entity.VocabWord;
import com.example.myvoca.entity.VocabWordId;
import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VocabWordRepository
    extends JpaRepository<VocabWord, VocabWordId> {

    List<VocabWord> findByVocab_vocabId(Integer vocabId);
    @Query("SELECT w FROM Word w WHERE w.id IN (" +
            "SELECT vw.word.id FROM VocabWord vw WHERE vw.vocab.id = :vocabId)")
    List<Word> findWordByVocabId(@Param("vocabId") Integer vocabId);
}
