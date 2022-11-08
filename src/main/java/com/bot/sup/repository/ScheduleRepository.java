package com.bot.sup.repository;

import com.bot.sup.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> getSchedulesByActivity_ActivityFormat_Id(Long id);

    @Modifying
    @Query("SELECT s FROM Schedule s " +
            "INNER JOIN Activity a on s.activity.id = a.id " +
            "INNER JOIN Route r on a.route.id = r.id " +
            "WHERE a.activityFormat.id = ?1 and s.eventDate = ?2")
    List<Schedule> selectScheduleByActivityFormatIdAndEventDate(Long id, LocalDate eventDate);

    void deleteScheduleById(Long id);
}
