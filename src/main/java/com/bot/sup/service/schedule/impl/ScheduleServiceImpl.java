package com.bot.sup.service.schedule.impl;

import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.service.activity.impl.ActivityServiceImpl;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ActivityRepository activityRepository;
    private final ActivityServiceImpl activityService;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getAllSchedule() {
        List<Schedule> schedules = new ArrayList<>(scheduleRepository.findAll());

        if (schedules.isEmpty()) {
            throw new EntityNotFoundException("Schedule not found");
        }

        return schedules;
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return findScheduleById(id);
    }

    @Override
    public Schedule createSchedule(ScheduleCreateDto createDto) {
        Schedule schedule = scheduleMapper.dtoToDomain(createDto);
        schedule.setActivity(activityService.getActivityById(createDto.getActivityId()));
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto) {
        Schedule scheduleById = findScheduleById(id);
        //TODO добавить set
        scheduleById.setActivity(activityService.getActivityById(scheduleCreateDto.getActivityId()));
        scheduleById.setEventDate(scheduleCreateDto.getEventDate());
        scheduleById.setEventTime(scheduleCreateDto.getEventTime());
        scheduleById.setParticipants(scheduleCreateDto.getParticipants());

        return scheduleRepository.save(scheduleById);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.delete(findScheduleById(id));
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id[" + id + "] not found"));
    }
}
