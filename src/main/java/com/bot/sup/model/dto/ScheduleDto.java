package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDto {
    private Long id;

    private ActivityDto activityDto;

    private LocalDateTime eventDate;
}
