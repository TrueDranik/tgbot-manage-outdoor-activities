package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.ActivityTypeRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

@Mapper(componentModel = "spring")
public abstract class ActivityMapper implements BaseMapper<Activity, ActivityDto> {
    @Autowired
    ActivityTypeRepository activityTypeRepository;
    @Autowired
    ActivityFormatRepository activityFormatRepository;

    @AfterMapping
    public void setRepoValues(ActivityDto dto, @MappingTarget Activity activity) {
        activity.setIsActive(true);
        activity.setActivityType(activityTypeRepository.findById(dto.getActivityType().getId())
                .orElseThrow(() -> new EntityNotFoundException("Activity type not found")));
        activity.setActivityFormat(activityFormatRepository.findById(dto.getActivityFormat().getId())
                .orElseThrow(() -> new EntityNotFoundException("Activity format not found")));
    }
}
