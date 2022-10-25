package com.bot.sup.service.activity.type;

import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityTypeService {
    List<ActivityType> getAllActivityType();

    ActivityType getActivityTypeById(Long id);

    ActivityType createActivityType(ActivityTypeCreateDto activityTypeCreateDto);

    ActivityType updateActivityType(Long id, ActivityTypeCreateDto activityTypeCreateDto);

    void deleteActivityType(Long id);
}
