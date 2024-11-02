package com.example.myvoca.repository;

import com.example.myvoca.entity.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabRepository
    extends JpaRepository<Vocab, Integer> {
    List<Vocab> findByUser_userId(Integer userId);
}
