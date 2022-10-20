package com.bot.sup.service;

import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityFormatService {
    private final ActivityFormatRepository activityFormatRepository;

    public void save(ActivityFormat activityFormat) {
        activityFormatRepository.save(activityFormat);
    }
}
