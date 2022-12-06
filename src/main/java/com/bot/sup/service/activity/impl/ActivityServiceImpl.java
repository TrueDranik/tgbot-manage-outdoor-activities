package com.bot.sup.service.activity.impl;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.specification.ActivitySpecification;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.activity.format.ActivityFormatService;
import com.bot.sup.service.activity.type.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityFormatRepository activityFormatRepository;
    private final ActivityFormatService activityFormatService;
    private final ActivityTypeService activityTypeService;
    private final ScheduleRepository scheduleRepository;

    public void save(Activity activity) {
        activityRepository.save(activity);
    }

    @Override
    public List<Activity> getAllActivity(ActivityRequestParams params) {
        return activityRepository.findAll(new ActivitySpecification(params));
    }

    @Override
    public Activity getActivityById(Long id) {
        return findActivityById(id);
    }

    @Override
    public Activity createActivity(ActivityCreateDto activityCreateDto) {
        if (activityCreateDto == null) {
            return null;
        }

        Activity activity = new Activity();

        activity.setName(activityCreateDto.getName());
        activity.setSeasonality(activityCreateDto.getSeasonality());
        activity.setDescription(activityCreateDto.getDescription());
        activity.setDuration(activityCreateDto.getDuration());
        activity.setAge(activityCreateDto.getAge());
        activity.setComplexity(activityCreateDto.getComplexity());
        activity.setPrice(activityCreateDto.getPrice());

        activity.setActivityType(activityTypeRepository.findById(activityCreateDto.getActivityTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Activity type not found")));
        activity.setActivityFormat(activityFormatRepository.findById(activityCreateDto.getActivityFormatId())
                .orElseThrow(() -> new EntityNotFoundException("Activity format not found")));

        return activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(Long id, ActivityCreateDto activityCreateDto) {
        Activity activityById = findActivityById(id);

        activityById.setName(activityCreateDto.getName());
        activityById.setSeasonality(activityCreateDto.getSeasonality());
        activityById.setActivityFormat(activityFormatService.getActivityFormatById(activityCreateDto.getActivityFormatId()));
        activityById.setActivityType(activityTypeService.getActivityTypeById(activityCreateDto.getActivityTypeId()));
        activityById.setDescription(activityCreateDto.getDescription());
        activityById.setDuration(activityCreateDto.getDuration());
        activityById.setAge(activityCreateDto.getAge());
        activityById.setComplexity(activityCreateDto.getComplexity());
        activityById.setPrice(activityCreateDto.getPrice());

        return activityRepository.save(activityById);
    }

    @Transactional
    @Override
    public void deleteActivity(Long id) {
        Activity activityById = findActivityById(id);
        activityById.setActive(false);

        List<Schedule> schedulesByActivityId = scheduleRepository.findSchedulesByActivity_Id(id);
        for (Schedule schedule :
                schedulesByActivityId) {
            schedule.setActive(false);
        }
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }
}
