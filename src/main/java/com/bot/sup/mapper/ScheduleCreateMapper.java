package com.bot.sup.mapper;

import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.RouteRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class ScheduleCreateMapper implements BaseMapper<Schedule, ScheduleCreateDto> {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    RouteRepository routeRepository;

    @AfterMapping
    public void setRepoValues(ScheduleCreateDto dto, @MappingTarget Schedule schedule) {
        schedule.setActivity(activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new EntityNotFoundException("Activity not found")));
        schedule.setRoute(routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found")));
        schedule.setIsActive(true);
    }
}
