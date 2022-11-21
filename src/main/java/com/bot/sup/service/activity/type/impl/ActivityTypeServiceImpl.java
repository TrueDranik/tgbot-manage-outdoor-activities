package com.bot.sup.service.activity.type.impl;

import com.bot.sup.mapper.ActivityTypeMapper;
import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.activity.type.ActivityTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityTypeServiceImpl implements ActivityTypeService {
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;

    public void save(ActivityType activityType) {
        activityTypeRepository.save(activityType);
    }

    @Override
    public List<ActivityType> getAllActivityType() {
        return new ArrayList<>(activityTypeRepository.findAll());
    }

    @Override
    public ActivityType getActivityTypeById(Long id) {
        return findActivityTypeById(id);
    }

    @Override
    public ActivityType createActivityType(ActivityTypeCreateDto activityTypeCreateDto) {
        ActivityType activityType = activityTypeMapper.dtoToDomain(activityTypeCreateDto);

        return activityTypeRepository.save(activityType);
    }

    @Override
    public ActivityType updateActivityType(Long id, ActivityTypeCreateDto activityTypeCreateDto) {
        ActivityType activityTypeById = findActivityTypeById(id);

        activityTypeById.setName(activityTypeCreateDto.getName());
        activityTypeById.setDescription(activityTypeCreateDto.getDescription());

        return activityTypeRepository.save(activityTypeById);
    }

    @Override
    public void deleteActivityType(Long id) {
        activityTypeRepository.delete(findActivityTypeById(id));
    }

    private ActivityType findActivityTypeById(Long id) {
        return activityTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity type with id[" + id + "] not found"));
    }
}
