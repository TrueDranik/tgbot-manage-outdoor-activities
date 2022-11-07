package com.bot.sup.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleCreateDto {
    private Long activityId;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Integer participants;
}
