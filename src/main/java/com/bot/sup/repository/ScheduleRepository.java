package com.bot.sup.repository;

import com.bot.sup.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Modifying
    @Query("SELECT s FROM Schedule s WHERE s.activity.activityFormat.id = ?1")
    public List<Schedule> selectScheduleByActivityFormatId(/*@Param("id")*/ Long id);

    @Query("SELECT s FROM Schedule s WHERE s.activity.route.name = ?1")
    public Optional<Schedule> selectOneScheduleByActivityFormatId(Long id);

    public List<Schedule> findByEventDate(LocalDate eventDate);
}
