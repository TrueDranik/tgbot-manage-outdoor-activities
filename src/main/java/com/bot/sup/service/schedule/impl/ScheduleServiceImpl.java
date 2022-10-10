package com.bot.sup.service.schedule.impl;

import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.activity.impl.ActivityServiceImpl;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ActivityServiceImpl activityService;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getAllSchedule() {
        return null;
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void createSchedule(ScheduleCreateDto createDto) {
        Schedule schedule = scheduleMapper.dtoToDomain(createDto);

        scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Long id, Schedule schedule) {
        return null;
    }

    @Override
    public void deleteSchedule(Long id) {

    }
}
