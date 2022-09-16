package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Route;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private Route routeId;
}
