package com.bot.sup.repository;

import com.bot.sup.model.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    boolean existsByNameEqualsIgnoreCase(String userAnswer);
}
