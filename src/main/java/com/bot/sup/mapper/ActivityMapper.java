package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityCreateDto;
import com.bot.sup.model.entity.Activity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends BaseMapper<Activity, ActivityCreateDto> {
}
