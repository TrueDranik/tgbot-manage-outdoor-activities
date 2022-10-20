package com.bot.sup.repository;

import com.bot.sup.model.entity.ActivityFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityFormatRepository extends JpaRepository<ActivityFormat, Long> {
    boolean existsByName(String userAnswer);
}
