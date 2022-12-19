package com.bot.sup.model.dto;

import lombok.Data;

@Data
public class CreateTourDto {
    private RouteDto route;
    private ScheduleDto scheduleDto;
    private ActivityDto activityDto;
}
