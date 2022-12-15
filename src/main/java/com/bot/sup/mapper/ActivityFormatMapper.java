package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatCreateDto> {

}
