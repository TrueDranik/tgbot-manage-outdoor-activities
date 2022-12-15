package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityTypeCreateDto;
import com.bot.sup.model.entity.ActivityType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityTypeMapper extends BaseMapper<ActivityType, ActivityTypeCreateDto> {

}
