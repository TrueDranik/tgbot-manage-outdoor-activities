package com.bot.sup.service.activity.type.impl;

import com.bot.sup.mapper.ActivityTypeMapper;
import com.bot.sup.model.dto.ActivityTypeDto;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.model.entity.ActivityType_;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.activity.type.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityTypeServiceImpl implements ActivityTypeService {
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;

    public void save(ActivityType activityType) {
        activityTypeRepository.save(activityType);
    }

    public boolean existsByNameEqualsIgnoreCase(String userAnswer) {
        return activityTypeRepository.existsByNameEqualsIgnoreCase(userAnswer);
    }

    public List<ActivityType> findAll() {
        return activityTypeRepository.findAll();
    }


    @Override
    public List<ActivityTypeDto> getAllActivityType() {
        List<ActivityType> activityTypes = activityTypeRepository.findAll();

        return activityTypeMapper.domainsToDtos(activityTypes);
    }

    @Override
    public ActivityTypeDto getActivityTypeById(Long id) {
        ActivityType activityTypeById = findActivityTypeById(id);

        return activityTypeMapper.domainToDto(activityTypeById);
    }

    @Override
    public ActivityTypeDto createActivityType(ActivityTypeDto activityTypeDto) {
        ActivityType activityType = activityTypeMapper.dtoToDomain(activityTypeDto);
        activityTypeRepository.save(activityType);

        return activityTypeMapper.domainToDto(activityType);
    }

    @Override
    public ActivityTypeDto updateActivityType(Long id, ActivityTypeDto activityTypeDto) {
        ActivityType activityTypeToUpdate = activityTypeMapper.dtoToDomain(activityTypeDto);
        ActivityType activityTypeFromDb = findActivityTypeById(id);

        BeanUtils.copyProperties(activityTypeToUpdate, activityTypeFromDb, ActivityType_.ID);
        activityTypeRepository.save(activityTypeFromDb);

        return activityTypeMapper.domainToDto(activityTypeFromDb);
    }

    @Override
    public void deleteActivityType(Long id) {
        ActivityType activityTypeById = findActivityTypeById(id);

        activityTypeRepository.delete(activityTypeById);
    }

    public ActivityType findActivityTypeById(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity type with id[" + id + "] not found"));
    }
}
