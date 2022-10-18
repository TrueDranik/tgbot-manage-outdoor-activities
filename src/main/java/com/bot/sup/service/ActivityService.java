package com.bot.sup.service;

import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public void save(Route route) {
        activityRepository.save(route);
    }
}
