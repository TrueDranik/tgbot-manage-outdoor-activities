package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends BaseMapper<Activity, ActivityCreateDto> {
    @Override
    ActivityCreateDto domainToDto(Activity domain);

    @Override
    Activity dtoToDomain(ActivityCreateDto activityCreateDto);

    @Override
    List<ActivityCreateDto> domainsToDtos(List<Activity> domains);

    @Override
    List<Activity> dtosToDomains(List<ActivityCreateDto> activityCreateDtos);
}
