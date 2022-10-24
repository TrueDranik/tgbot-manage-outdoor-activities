package com.bot.sup.model.dto;

import lombok.Data;

@Data
public class RouteCreateDto {
    private Long id;
    private String name;
    private String startPointCoordinates;
    private String startPointName;
    private String finishPointCoordinates;
    private String finishPointName;
    private String mapLink;
    private String length;
}
