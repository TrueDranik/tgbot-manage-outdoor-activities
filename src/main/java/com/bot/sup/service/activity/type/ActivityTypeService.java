package com.bot.sup.service.activity.type;

import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityTypeService {
    private final ActivityTypeRepository activityTypeRepository;

    public void save(ActivityType activityType){
        activityTypeRepository.save(activityType);
    }
}
