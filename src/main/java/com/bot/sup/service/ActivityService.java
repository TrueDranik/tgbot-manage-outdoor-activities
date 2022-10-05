package com.bot.sup.service;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public void save(Activity activity) {
        activityRepository.save(activity);
    }
}
