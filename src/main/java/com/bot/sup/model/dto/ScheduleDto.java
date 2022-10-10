package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDto {
    private Long id;

    private ActivityDto activityDto;

    private LocalDateTime eventDate;

    public ScheduleDto(LocalDateTime eventDate, ActivityDto activityId) {
        this.eventDate = eventDate;
        this.activityDto = activityId;
    }
}
