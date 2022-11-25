package com.bot.sup.service.schedule.impl;

import com.bot.sup.mapper.ScheduleMapper;
import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.specification.ScheduleSpecification;
import com.bot.sup.service.activity.impl.ActivityServiceImpl;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ActivityServiceImpl activityService;
    private final ScheduleMapper scheduleMapper;
    private final RouteRepository routeRepository;

    @Override
    public List<Schedule> getAllSchedule(ScheduleRequestParams params) {
        return scheduleRepository.findAll(new ScheduleSpecification(params));
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return findScheduleById(id);
    }

    @Transactional
    @Override
    public List<Schedule> createSchedule(List<ScheduleCreateDto> createDto) {
        if (createDto == null) {
            return null;
        }

        List<Schedule> schedules = new ArrayList<>();

        for (int i = 0; i <= createDto.size() - 1; i++) {
            Schedule schedule = new Schedule();
            schedule.setEventDate(createDto.get(i).getEventDate());
            schedule.setEventTime(createDto.get(i).getEventTime());
            schedule.setParticipants(createDto.get(i).getParticipants());

            schedule.setActivity(activityService.getActivityById(createDto.get(i).getActivityId()));
            schedule.setRoute(routeRepository.findById(createDto.get(i).getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route not found")));

            schedules.add(schedule);
        }

        return scheduleRepository.saveAll(schedules);
    }

    @Override
    public Schedule updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto) {
        Schedule scheduleById = findScheduleById(id);

        scheduleById.setActivity(activityService.getActivityById(scheduleCreateDto.getActivityId()));
        scheduleById.setEventDate(scheduleCreateDto.getEventDate());
        scheduleById.setEventTime(scheduleCreateDto.getEventTime());
        scheduleById.setParticipants(scheduleCreateDto.getParticipants());
        scheduleById.setRoute(routeRepository.findById(scheduleCreateDto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found")));

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
