package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityTypeDto {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
}
