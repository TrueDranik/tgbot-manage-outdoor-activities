package com.bot.sup.service.schedule;

import com.bot.sup.model.ScheduleParams;
import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface ScheduleService {
    ScheduleDto getScheduleByTelegramId(Long telegramId);

    List<ScheduleDto> getAllSchedule(ScheduleRequestParams params);

    ScheduleDto getScheduleById(Long id);

    List<ScheduleCreateDto> createSchedule(List<ScheduleCreateDto> createDto);

    ScheduleCreateDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto);

    List<ScheduleDto> getAllFilteredSchedule(ScheduleParams scheduleParams);

    List<ScheduleDto> getAllFilteredSchedule(Boolean isActive, LocalDate eventDate, LocalTime eventTime);

    void deleteSchedule(Long id);
}
