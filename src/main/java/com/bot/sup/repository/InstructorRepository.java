package com.bot.sup.repository;

import com.bot.sup.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByTelegramId(Long telegramId);

    void deleteByTelegramId(Long telegramId);

    boolean existsByTelegramId(Long telegramId);
}

