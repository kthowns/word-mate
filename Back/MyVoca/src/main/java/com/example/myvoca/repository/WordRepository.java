package com.example.myvoca.repository;

import com.example.myvoca.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository
    extends JpaRepository<Word, Integer> {
}
