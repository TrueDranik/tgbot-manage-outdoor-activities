package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleCreateDto {
    private Activity activity;
    private LocalDateTime eventDate;
}
