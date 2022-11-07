package com.bot.sup.mapper;

import com.bot.sup.model.dto.ActivityFormatCreateDto;
import com.bot.sup.model.entity.ActivityFormat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityFormatMapper extends BaseMapper<ActivityFormat, ActivityFormatCreateDto> {
    @Override
    ActivityFormatCreateDto domainToDto(ActivityFormat domain);

    @Override
    ActivityFormat dtoToDomain(ActivityFormatCreateDto activityFormatCreateDto);

    @Override
    List<ActivityFormatCreateDto> domainsToDtos(List<ActivityFormat> domains);

    @Override
    List<ActivityFormat> dtosToDomains(List<ActivityFormatCreateDto> activityFormatCreateDtos);
}
