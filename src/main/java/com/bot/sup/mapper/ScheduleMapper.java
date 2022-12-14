package com.bot.sup.mapper;

import com.bot.sup.model.dto.ScheduleCreateDto;
import com.bot.sup.model.dto.ScheduleDto;
import com.bot.sup.model.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper extends BaseMapper<Schedule, ScheduleCreateDto> {
    Schedule toEntity(ScheduleDto scheduleDto);

    @Override
    ScheduleCreateDto domainToDto(Schedule domain); //TODO: Удалить override методы, т.к точно такие уже были созданы

    @Override
    Schedule dtoToDomain(ScheduleCreateDto scheduleCreateDto);

    @Override
    List<ScheduleCreateDto> domainsToDtos(List<Schedule> domains);

    @Override
    List<Schedule> dtosToDomains(List<ScheduleCreateDto> scheduleCreateDtos);
}
