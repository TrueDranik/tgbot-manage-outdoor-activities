package com.bot.sup.service.activity.impl;

import com.bot.sup.mapper.ActivityMapper;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.route.impl.RouteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final RouteServiceImpl routeService;

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public List<Activity> getAllActivity() {
        return null;
    }

    @Override
    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void createActivity(ActivityCreateDto createDto) {
        Activity activity = activityMapper.dtoToDomain(createDto);

        activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(Long id, Activity activity) {
        return null;
    }

    @Override
    public void deleteActivity(Long id) {

    }
}
