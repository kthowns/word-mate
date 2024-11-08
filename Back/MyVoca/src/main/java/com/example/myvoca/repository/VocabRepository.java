package com.example.myvoca.repository;

import com.example.myvoca.entity.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabRepository
    extends JpaRepository<Vocab, Integer> {
    @Query("SELECT v FROM Vocab v where v.user.userId = :userId")
    List<Vocab> findByUserId(@Param("userId") Integer userId);

    @Query("select count(w) from Word w where w.vocab.vocabId = :vocabId")
    Integer countWords(@Param("vocabId") Integer vocabId);
}
