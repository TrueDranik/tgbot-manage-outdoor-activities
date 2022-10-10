package com.bot.sup.service;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
