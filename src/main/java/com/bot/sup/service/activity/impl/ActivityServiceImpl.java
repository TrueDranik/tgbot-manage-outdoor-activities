package com.bot.sup.service.activity.impl;

import com.bot.sup.mapper.ActivityMapper;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.activity.format.ActivityFormatService;
import com.bot.sup.service.activity.type.ActivityTypeService;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final RouteService routeService;
    private final ActivityFormatService activityFormatService;
    private final ActivityTypeService activityTypeService;

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public List<Activity> getAllActivity() {
        List<Activity> activities = activityRepository.findAll();

        if (activities.isEmpty()) {
            throw new EntityNotFoundException("Activities not found");
        }

        return activities;
    }

    @Override
    public Activity getActivityById(Long id) {
        return findActivityById(id);
    }

    @Override
    public Activity createActivity(ActivityCreateDto createDto) {
        Activity activity = activityMapper.dtoToDomain(createDto);

        activity.setActivityFormat(activityFormatService.getActivityFormatById(createDto.getActivityFormatId()));
        activity.setActivityType(activityTypeService.getActivityTypeById(createDto.getActivityTypeId()));
        activity.setRoute(routeService.getRouteById(createDto.getRouteId()));

        return activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(Long id, ActivityCreateDto activityCreateDto) {
        Activity activityById = findActivityById(id);
        activityById.setName(activityCreateDto.getName());
        activityById.setSeasonality(activityCreateDto.getSeasonality());
        activityById.setActivityFormat(activityFormatService.getActivityFormatById(activityCreateDto.getActivityFormatId()));
        activityById.setActivityType(activityTypeService.getActivityTypeById(activityCreateDto.getActivityTypeId()));
        activityById.setDescription(activityCreateDto.getDescription());
        activityById.setRoute(routeService.getRouteById(activityCreateDto.getRouteId()));
        activityById.setDuration(activityCreateDto.getDuration());
        activityById.setAge(activityCreateDto.getAge());
        activityById.setComplexity(activityCreateDto.getComplexity());
        activityById.setPrice(activityCreateDto.getPrice());

        return activityRepository.save(activityById);
    }

    @Override
    public void deleteActivity(Long id) {
        activityRepository.delete(findActivityById(id));
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }
}
