package com.bot.sup.service.activity.impl;

import com.bot.sup.mapper.ActivityMapper;
import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.dto.ActivityCreateDtoWithoutRoute;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.repository.specification.ActivitySpecification;
import com.bot.sup.service.activity.ActivityService;
import com.bot.sup.service.activity.format.ActivityFormatService;
import com.bot.sup.service.activity.type.ActivityTypeService;
import com.bot.sup.service.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityFormatRepository activityFormatRepository;
    private final RouteService routeService;
    private final ActivityFormatService activityFormatService;
    private final ActivityTypeService activityTypeService;

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
    public Activity createActivity(ActivityCreateDtoWithoutRoute activityCreateDto) {
        if ( activityCreateDto == null ) {
            return null;
        }

        Activity activity = new Activity();

        activity.setName( activityCreateDto.getName() );
        activity.setSeasonality( activityCreateDto.getSeasonality() );
        activity.setDescription( activityCreateDto.getDescription() );
        activity.setDuration( activityCreateDto.getDuration() );
        activity.setAge( activityCreateDto.getAge() );
        activity.setComplexity( activityCreateDto.getComplexity() );
        activity.setPrice( activityCreateDto.getPrice() );

//        activity.setActivityFormat(activityFormatService.getActivityFormatById(activityCreateDto.getActivityFormatId()));
//        activity.setActivityType(activityTypeService.getActivityTypeById(activityCreateDto.getActivityTypeId()));
        activity.setActivityType(activityTypeRepository.findById(activityCreateDto.getActivityTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Activity type not found")));
        activity.setActivityFormat(activityFormatRepository.findById(activityCreateDto.getActivityFormatId())
                .orElseThrow(() -> new EntityNotFoundException("Activity format not found")));

        return activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(Long id, ActivityCreateDtoWithoutRoute activityCreateDto) {
        Activity activityById = findActivityById(id);

        activityById.setName(activityCreateDto.getName());
        activityById.setSeasonality(activityCreateDto.getSeasonality());
        activityById.setActivityFormat(activityFormatService.getActivityFormatById(activityCreateDto.getActivityFormatId()));
        activityById.setActivityType(activityTypeService.getActivityTypeById(activityCreateDto.getActivityTypeId()));
        activityById.setDescription(activityCreateDto.getDescription());
        //activityById.setRoute(routeService.getRouteById(activityCreateDto.getRouteId()));
        activityById.setDuration(activityCreateDto.getDuration());
        activityById.setAge(activityCreateDto.getAge());
        activityById.setComplexity(activityCreateDto.getComplexity());
        activityById.setPrice(activityCreateDto.getPrice());

        return activityRepository.save(activityById);
    }

    @Override
    public void deleteActivity(Long id) {
        activityRepository.delete(findActivityById(id));
    }

    private Activity findActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity with id[" + id + "] not found"));
    }
}
