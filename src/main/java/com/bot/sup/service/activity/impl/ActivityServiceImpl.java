package com.bot.sup.service.activity.impl;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.specification.ActivitySpecification;
import com.bot.sup.service.activity.ActivityService;
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
    private final ScheduleRepository scheduleRepository;

//    public void save(Activity activity) {
//        activityRepository.save(activity);
//    }

    @Override
    public List<ActivityDto> getAllActivity(ActivityRequestParams params) {
        return activityRepository.findAll(new ActivitySpecification(params));
    }

    @Override
    public ActivityDto getActivityById(Long id) {
        Activity activityById = findActivityById(id);

        return activityToActivityDto(activityById);
    }

    @Override
    public ActivityDto createActivity(ActivityCreateDto activityCreateDto) {
        if (activityCreateDto == null) {
            return null;
        }

        Activity activity = new Activity();

        activityDtoToActivity(activityCreateDto, activity);

        return activityToActivityDto(activity);
    }

    @Override
    public ActivityDto updateActivity(Long id, ActivityCreateDto activityCreateDto) {
        Activity activity = findActivityById(id);

        activityDtoToActivity(activityCreateDto, activity);

        return activityToActivityDto(activity);
    }

    @Transactional
    @Override
    public void deleteActivity(Long id) {
        Activity activityById = findActivityById(id);
        activityById.setIsActive(false);

        //todo подумай над запросом
        List<Schedule> schedulesByActivityId = scheduleRepository.findSchedulesByActivity_Id(id);
        for (Schedule schedule : schedulesByActivityId) {
            schedule.setIsActive(false);
        }
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }

    private static ActivityDto activityToActivityDto(Activity activity) {
        ActivityDto activityDto = new ActivityDto();

        activityDto.setId(activity.getId());
        activityDto.setName(activity.getName());
        activityDto.setSeasonality(activity.getSeasonality());
        activityDto.setDescription(activity.getDescription());
        activityDto.setDuration(activity.getDuration());
        activityDto.setAge(activity.getAge());
        activityDto.setComplexity(activity.getComplexity());
        activityDto.setPrice(activity.getPrice());
        activityDto.setIsActive(activity.getIsActive());
        activityDto.setActivityFormat(activity.getActivityFormat());
        activityDto.setActivityType(activity.getActivityType());

        return activityDto;
    }

    private void activityDtoToActivity(ActivityCreateDto activityCreateDto, Activity activity) {
        activity.setName(activityCreateDto.getName());
        activity.setSeasonality(activityCreateDto.getSeasonality());
        activity.setDescription(activityCreateDto.getDescription());
        activity.setDuration(activityCreateDto.getDuration());
        activity.setAge(activityCreateDto.getAge());
        activity.setComplexity(activityCreateDto.getComplexity());
        activity.setPrice(activityCreateDto.getPrice());
        activity.setIsActive(true);

        activity.setActivityType(activityTypeRepository.findById(activityCreateDto.getActivityTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Activity type not found")));
        activity.setActivityFormat(activityFormatRepository.findById(activityCreateDto.getActivityFormatId())
                .orElseThrow(() -> new EntityNotFoundException("Activity format not found")));

        activityRepository.save(activity);
    }
}
