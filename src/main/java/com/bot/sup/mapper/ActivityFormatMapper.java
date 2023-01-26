package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityFormatDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatDto> {
    @Override
    @Mapping(target = "id", ignore = true)
    ActivityFormat dtoToDomain(ActivityFormatDto activityFormatDto);
}
