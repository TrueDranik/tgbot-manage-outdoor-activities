package com.bot.sup.service.activity;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.dto.ActivityCreateDtoWithoutRoute;
import com.bot.sup.model.entity.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {
    List<Activity> getAllActivity(ActivityRequestParams params);

    Activity getActivityById(Long id);

    Activity createActivity(ActivityCreateDtoWithoutRoute activityCreateDto);

    Activity updateActivity(Long id, ActivityCreateDtoWithoutRoute activityCreateDto);

    void deleteActivity(Long id);
}
