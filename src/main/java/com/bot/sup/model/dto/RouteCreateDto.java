package com.bot.sup.model.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RouteCreateDto {
    private String name;
    private String seasonality;
    private String activityForm;
    private String activityType;
    private String description;
    private String location;
    private LocalTime duration;
}
