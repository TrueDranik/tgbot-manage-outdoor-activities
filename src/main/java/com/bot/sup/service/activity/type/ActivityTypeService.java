package com.bot.sup.service.activity.type;

import com.bot.sup.model.dto.ActivityTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityTypeService {
    List<ActivityTypeDto> getAllActivityType();

    ActivityTypeDto getActivityTypeById(Long id);

    ActivityTypeDto createActivityType(ActivityTypeDto activityTypeCreateDto);

    ActivityTypeDto updateActivityType(Long id, ActivityTypeDto activityTypeCreateDto);

    void deleteActivityType(Long id);
}
