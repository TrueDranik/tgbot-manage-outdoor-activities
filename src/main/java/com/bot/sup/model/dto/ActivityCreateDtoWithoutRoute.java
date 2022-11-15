package com.bot.sup.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivityCreateDtoWithoutRoute {
    private String name;
    private String seasonality;
    private Long activityFormatId;
    private Long activityTypeId;
    private String description;
    private String duration;
    private String age;
    private String complexity;
    private BigDecimal price;
}
