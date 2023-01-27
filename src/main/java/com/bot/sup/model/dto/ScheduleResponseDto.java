package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Schema(title = "Расписание")
public class ScheduleResponseDto {
    private Long id;
    @Schema(title = "id активности", defaultValue = "1")
    private Long activityId;
    @Schema(title = "Дата старта", defaultValue = "1970-01-01")
    private LocalDate eventDate;
    @Schema(title = "Время старта", defaultValue = "11:11:11")
    private LocalTime eventTime;
    @Schema(title = "Количество мест", defaultValue = "0", nullable = true)
    private Integer participants;
    @Schema(title = "id маршрута", defaultValue = "1")
    private Long routeId;
}
