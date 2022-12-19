package com.bot.sup.service.schedule;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    List<ScheduleDto> getAllSchedule(ScheduleRequestParams params);

    ScheduleDto getScheduleById(Long id);

    List<ScheduleDto> createSchedule(List<ScheduleCreateDto> createDto);

    ScheduleDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto);

    void deleteSchedule(Long id);
}
