package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Client;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.model.entity.Route;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class ScheduleDto {
    private Long id;
    private Activity activity;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Integer participants;
    private Route route;
    private Set<Client> client;
    private Set<Instructor> instructor;
    private Boolean isActive;
}
