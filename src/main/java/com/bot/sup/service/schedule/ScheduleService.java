package com.bot.sup.service.schedule;

import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    List<Schedule> getAllSchedule();

    Schedule getScheduleById(Long id);

    Schedule createSchedule(ScheduleCreateDto createDto);

    Schedule updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto);

    void deleteSchedule(Long id);
}
