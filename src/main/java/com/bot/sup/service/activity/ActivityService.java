package com.bot.sup.service.activity;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {
    List<ActivityDto> getAllActivity(ActivityRequestParams params);

    ActivityDto getActivityById(Long id);

    Activity createActivity(ActivityCreateDto activityCreateDto);

    Activity updateActivity(Long id, ActivityCreateDto activityCreateDto);

    void deleteActivity(Long id);
}
