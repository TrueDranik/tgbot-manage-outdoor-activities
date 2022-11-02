package com.bot.sup.service.activity;

import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {
    List<Activity> getAllActivity();

    Activity getActivityById(Long id);

    Activity createActivity(ActivityCreateDto createDto);

    Activity updateActivity(Long id, ActivityCreateDto activityCreateDto);

    void deleteActivity(Long id);
}
