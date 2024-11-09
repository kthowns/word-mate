package com.example.myvoca.repository;

import com.example.myvoca.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemeRepository
    extends JpaRepository<Theme, Integer> {
    @Query("SELECT t FROM Theme t where t.user.userId = :userId")
    public List<Theme> findByUserId(@Param("userId") Integer userId);
}
