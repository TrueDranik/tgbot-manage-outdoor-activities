package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityTypeMapper extends BaseMapper<ActivityType, ActivityTypeCreateDto> {
    @Override
    ActivityTypeCreateDto domainToDto(ActivityType domain);

    @Override
    ActivityType dtoToDomain(ActivityTypeCreateDto activityTypeCreateDto);

    @Override
    List<ActivityTypeCreateDto> domainsToDtos(List<ActivityType> domains);

    @Override
    List<ActivityType> dtosToDomains(List<ActivityTypeCreateDto> activityTypeCreateDtos);
}
