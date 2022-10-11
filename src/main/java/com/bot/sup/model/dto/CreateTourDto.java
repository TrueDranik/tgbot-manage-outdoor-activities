package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Schedule;
import lombok.Data;

@Data
public class CreateTourDto {
    private RouteDto route;
    private ScheduleDto scheduleDto;
    private ActivityDto activityDto;
}
