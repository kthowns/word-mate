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

    @Query("SELECT vw from VocabWord vw where vw.word.wordId = :wordId")
    List<VocabWord> findByWordId(@Param("wordId") Integer wordId);
    @Query("SELECT COUNT(vw) from VocabWord vw where vw.vocab.vocabId = :vocabId")
    Integer getCountByVocabId(@Param("vocabId") Integer vocabId);
}
