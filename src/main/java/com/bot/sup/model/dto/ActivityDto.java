package com.bot.sup.model.dto;

import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivityDto {
    private Long id;
    private String name;
    private String seasonality;
    private ActivityFormat activityFormat;
    private ActivityType activityType;
    private String description;
    private String duration;
    private String age;
    private String complexity;
    private BigDecimal price;
    private Boolean isActive;
}
