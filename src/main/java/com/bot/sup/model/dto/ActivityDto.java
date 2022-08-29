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

    private String seasonality;

    private String activityFormat;

    private String activityType;

    private String description;

    private String venue;       // место проведения

    @ManyToOne
    private Route route;

    private LocalDateTime duration;
}
