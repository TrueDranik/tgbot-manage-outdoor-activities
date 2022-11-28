package com.bot.sup.service.schedule;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    Schedule getScheduleByTelegramId(Long telegramId);

    List<Schedule> getAllSchedule(ScheduleRequestParams params);

    Schedule getScheduleById(Long id);

    List<Schedule> createSchedule(List<ScheduleCreateDto> createDto);

    Schedule updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto);

    void deleteSchedule(Long id);
}
