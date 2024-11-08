package com.example.myvoca.repository;

import com.example.myvoca.entity.WordStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatsRepository
        extends JpaRepository<WordStats, Integer> {

    @Query("select ws from WordStats ws where ws.word.vocab.vocabId = :vocabId")
    List<WordStats> findWordStatesByVocabId(@Param("vocabId") Integer vocabId);
}
