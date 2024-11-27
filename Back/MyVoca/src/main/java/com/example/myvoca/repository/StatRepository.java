package com.example.myvoca.repository;

import com.example.myvoca.entity.Stat;
import com.example.myvoca.entity.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatRepository
        extends JpaRepository<Stat, Integer> {

    @Query("select s from Stat s where s.word.vocab = :vocab")
    List<Stat> findByVocab(@Param("vocab") Vocab vocab);

    @Query("select (1.0*sum(s.isLearned)) / count(w)"+
    " from Word w join Stat s"+
    " on w = s.word"+
    " where w.vocab = :vocab")
    double getLearningRateByVocab(@Param("vocab") Vocab vocab);
}