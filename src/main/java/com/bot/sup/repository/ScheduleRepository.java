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
    List<Schedule> findByEventDate(LocalDate eventDate);

    @Modifying
    @Query("SELECT s FROM Schedule s WHERE s.activity.activityFormat.id = ?1")
    List<Schedule> selectScheduleByActivityFormatId(Long id);

    @Modifying
    @Query("SELECT s FROM Schedule s " +
            "INNER JOIN Activity a on s.activity.id = a.id " +
            "INNER JOIN Route r on a.route.id = r.id " +
            "WHERE a.activityFormat.id = ?1 and s.eventDate = ?2")
    List<Schedule> selectScheduleByActivityFormatIdAndEventDate(Long id, LocalDate eventDate);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.id = ?1")
    void deleteScheduleByIdQuery(Long id);
}
