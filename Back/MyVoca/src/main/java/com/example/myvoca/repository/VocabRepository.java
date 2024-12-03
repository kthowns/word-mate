package com.example.myvoca.repository;

import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabRepository
    extends JpaRepository<Vocab, Integer> {
    List<Vocab> findByUser(User user);

    @Query("select count(w) from Word w where w.vocab.vocabId = :vocabId")
    Integer countWords(@Param("vocabId") Integer vocabId);

    Optional<Vocab> findByTitleAndDescriptionAndUser(String title, String description, User user);
}