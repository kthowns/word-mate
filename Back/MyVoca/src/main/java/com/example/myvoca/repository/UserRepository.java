package com.example.myvoca.repository;

import com.example.myvoca.entity.User;
import com.example.myvoca.entity.Vocab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
    extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
