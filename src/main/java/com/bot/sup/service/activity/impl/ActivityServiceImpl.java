package com.bot.sup.service.activity.impl;

import com.bot.sup.mapper.ActivityMapper;
import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityDto;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Activity_;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ScheduleRepository;
import com.bot.sup.repository.specification.ActivitySpecification;
import com.bot.sup.service.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ScheduleRepository scheduleRepository;
    private final ActivityMapper activityMapper;


    @Override
    public List<ActivityDto> getAllActivity(ActivityRequestParams params) {
        return activityRepository.findAll(new ActivitySpecification(params));
    }

    @Override
    public ActivityDto getActivityById(Long id) {
        Activity activityById = findActivityById(id);

        return activityMapper.domainToDto(activityById);
    }

    @Override
    public ActivityDto createActivity(ActivityDto activityDto) {
        Activity activity = activityMapper.dtoToDomain(activityDto);
        activityRepository.save(activity);

        return activityMapper.domainToDto(activity);
    }

    @Override
    public ActivityDto updateActivity(Long id, ActivityDto activityDto) {
        Activity activityForUpdate = activityMapper.dtoToDomain(activityDto);
        Activity activityFromDb = findActivityById(id);

        BeanUtils.copyProperties(activityForUpdate, activityFromDb, Activity_.ID, Activity_.IS_ACTIVE);
        activityRepository.save(activityFromDb);

        return activityMapper.domainToDto(activityFromDb);
    }


    @Transactional
    @Override
    public void deleteActivity(Long id) {
        Activity activityById = findActivityById(id);
        activityById.setIsActive(false);
        activityRepository.save(activityById);

        scheduleRepository.setScheduleInactiveByActivityId(id);
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }
}
