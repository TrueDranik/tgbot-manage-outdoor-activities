package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Instructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDto {
    private Long id;

    private Activity activityId;

    private LocalDateTime eventDate;
}
