package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RouteDto {
    private Long id;

    private String name;

    private String seasonality;

    private String activityForm;

    private String activityType;

    private String description;

    private String location;

    private LocalTime duration;
}
