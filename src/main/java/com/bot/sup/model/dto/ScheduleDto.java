package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Instructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleDto {
    private Long id;

    private LocalDate eventDate;

    private Activity activity;

    private Instructor instructor;

    private Booking booking;
}
