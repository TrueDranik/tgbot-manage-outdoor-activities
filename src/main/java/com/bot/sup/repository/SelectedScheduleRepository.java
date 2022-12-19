package com.bot.sup.repository;

import com.bot.sup.model.entity.SelectedSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SelectedScheduleRepository extends JpaRepository<SelectedSchedule, Long> {
    Optional<SelectedSchedule> findByTelegramId(Long telegramId);

    void deleteByTelegramId(Long telegramId);
}
