package com.bot.sup.mapper;

import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.entity.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper extends BaseMapper<Schedule, ScheduleCreateDto> {

}
