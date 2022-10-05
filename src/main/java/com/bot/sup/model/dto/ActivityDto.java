package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDto {
    private Long id;

    private String name;

    private String startPointCoordinates;

    private String startPointName;

    private String finishPointCoordinates;

    private String finishPointName;

    private String mapLink;

    private RouteDto routeDto;
}
