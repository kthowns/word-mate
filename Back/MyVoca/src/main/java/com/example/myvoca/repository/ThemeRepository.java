package com.example.myvoca.repository;

import com.example.myvoca.entity.Theme;
import com.example.myvoca.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ThemeRepository
    extends JpaRepository<Theme, Integer> {
    List<Theme> findByUser(User userById);
}
