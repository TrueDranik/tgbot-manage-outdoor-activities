package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Route;
import lombok.Data;

@Data
public class ActivityCreateDto {
    private String name;
    private String startPointCoordinates;
    private String startPointName;
    private String finishPointCoordinates;
    private String finishPointName;
    private String mapLink;
    private Route route;
}
