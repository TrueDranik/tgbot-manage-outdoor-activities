package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto {
    private Long id;
    private String seasonality;
    private ActivityFormat activityFormat;
    private ActivityType activityType;
    private String description;
    private Activity route;
    private String duration;
    private String age;
    private String complexity;
    private String price;
}
