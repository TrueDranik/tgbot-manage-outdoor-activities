package com.bot.sup.service.schedule.impl;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.model.entity.SelectedSchedule;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.SelectedScheduleRepository;
import com.bot.sup.repository.specification.ScheduleSpecification;
import com.bot.sup.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final ActivityRepository activityRepository;
    private final SelectedScheduleRepository selectedScheduleRepository;

    @Override
    public ScheduleDto getScheduleByTelegramId(Long telegramId) {
        Optional<SelectedSchedule> byTelegramId = Optional.ofNullable(selectedScheduleRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new EntityNotFoundException("User with Telegram id[" + telegramId + "] didn't selecte schedule")));
        Long scheduleId = byTelegramId.get().getCurrentScheduleId();

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id[" + scheduleId + "not found"));
        return scheduleToDto(schedule);
    }

    @Override
    public List<ScheduleDto> getAllSchedule(ScheduleRequestParams params) {
        return scheduleRepository.findAll(new ScheduleSpecification(params));
    }

    @Override
    public ScheduleDto getScheduleById(Long id) {
        Schedule scheduleById = findScheduleById(id);

        return scheduleToDto(scheduleById);
    }

    @Transactional
    @Override
    public List<ScheduleDto> createSchedule(List<ScheduleCreateDto> scheduleCreateDto) {
        if (scheduleCreateDto == null) {
            return Collections.emptyList();
        }

        List<Schedule> schedules = new ArrayList<>(scheduleCreateDto.size());
        for (ScheduleCreateDto value : scheduleCreateDto) {
            schedules.add(dtoToSchedule(value));
        }
        scheduleRepository.saveAll(schedules);

        List<ScheduleDto> scheduleDtos = new ArrayList<>(scheduleCreateDto.size());
        for (Schedule scheduleDto : schedules) {
            scheduleDtos.add(scheduleToDto(scheduleDto));
        }

        return scheduleDtos;
    }

    @Override
    public ScheduleDto updateSchedule(Long id, ScheduleCreateDto scheduleCreateDto) {
        Schedule scheduleById = findScheduleById(id);

        scheduleById.setActivity(activityRepository.findById(scheduleCreateDto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found")));
        scheduleById.setEventDate(scheduleCreateDto.getEventDate());
        scheduleById.setEventTime(scheduleCreateDto.getEventTime());
        scheduleById.setParticipants(scheduleCreateDto.getParticipants());
        scheduleById.setRoute(routeRepository.findById(scheduleCreateDto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found")));
        scheduleById.setIsActive(true);

        scheduleRepository.save(scheduleById);

        return scheduleToDto(scheduleById);
    }

    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        Schedule scheduleById = findScheduleById(id);
        scheduleById.setIsActive(false);

        scheduleRepository.save(scheduleById);
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id[" + id + "] not found"));
    }

    private static ScheduleDto scheduleToDto(Schedule scheduleById) {
        ScheduleDto scheduleDto = new ScheduleDto();

        scheduleDto.setId(scheduleById.getId());
        scheduleDto.setActivity(scheduleById.getActivity());
        scheduleDto.setEventDate(scheduleById.getEventDate());
        scheduleDto.setEventTime(scheduleById.getEventTime());
        scheduleDto.setParticipants(scheduleById.getParticipants());
        scheduleDto.setRoute(scheduleById.getRoute());
        scheduleDto.setClient(scheduleById.getClient());
        scheduleDto.setInstructor(scheduleById.getInstructor());
        scheduleDto.setIsActive(scheduleById.getIsActive());

        return scheduleDto;
    }

    private Schedule dtoToSchedule(ScheduleCreateDto scheduleCreateDto) {
        Schedule schedule = new Schedule();
        schedule.setActivity(activityRepository.findById(scheduleCreateDto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found")));
        schedule.setEventDate(scheduleCreateDto.getEventDate());
        schedule.setEventTime(scheduleCreateDto.getEventTime());
        schedule.setParticipants(scheduleCreateDto.getParticipants());
        schedule.setRoute(routeRepository.findById(scheduleCreateDto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found")));
        schedule.setIsActive(true);

        return schedule;
    }
}
