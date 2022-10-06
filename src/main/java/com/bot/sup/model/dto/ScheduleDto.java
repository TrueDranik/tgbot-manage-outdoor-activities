package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDto {
    private Long id;

    private Activity activityId;

    private LocalDateTime eventDate;
}
