package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.dto.ActivityFormatDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatCreateDto> {
    List<ActivityFormatDto> ActivityFormatToDto(List<ActivityFormat> domains);
}
