package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "seasonality")
    private String seasonality;

    @Column(name = "activity_form")
    private String activityForm;

    @Column(name = "activity_type", length = 50)
    private String activityType;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "duration")
    private LocalTime duration;
}
