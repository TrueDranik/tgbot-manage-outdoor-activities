package com.bot.sup.mapper;

import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(ScheduleDto scheduleDto);
}
