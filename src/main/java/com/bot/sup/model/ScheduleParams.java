package com.bot.sup.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ScheduleParams {

    private Boolean isActive;
    @Schema(title = "Дата старта", defaultValue = "1970-01-01")
    private LocalDate eventDate;
    @Schema(title = "Время старта", defaultValue = "11:11:11")
    private LocalTime eventTime;

    public ScheduleParams(Boolean isActive, LocalDate eventDate, LocalTime eventTime) {
        this.eventDate = eventDate;
        this.isActive = isActive;
        this.eventTime = eventTime;

    }
}
