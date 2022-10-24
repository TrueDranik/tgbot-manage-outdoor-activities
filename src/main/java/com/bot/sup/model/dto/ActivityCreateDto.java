package com.bot.sup.model.dto;

import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.model.entity.Route;
import lombok.Data;

@Data
public class ActivityCreateDto {
    private Long id;
    private String seasonality;
    private ActivityFormat activityFormat;
    private ActivityType activityType;
    private String description;
    private Route route;
    private String duration;
    private String age;
    private String complexity;
    private String price;
}
