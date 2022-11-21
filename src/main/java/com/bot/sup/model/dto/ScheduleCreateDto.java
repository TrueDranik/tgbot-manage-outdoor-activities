package com.bot.sup.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(title = "Расписание")
public class ScheduleCreateDto {
    @Schema(title = "id активности", defaultValue = "1")
    private Long activityId;
    @Schema(title = "Дата старта", defaultValue = "1970-01-01")
    private LocalDate eventDate;
    @Schema(title = "Время старта", defaultValue = "11:11:11")
    private LocalTime eventTime;
    @Schema(title = "Количество мест", defaultValue = "0", nullable = true)
    private Integer participants;
}
