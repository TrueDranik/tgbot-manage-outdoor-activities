package com.bot.sup.repository;

import com.bot.sup.model.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    boolean existsByNameEqualsIgnoreCase(String userAnswer);
    List<ActivityType> findActivityTypesByIsActiveIsTrue();
}
