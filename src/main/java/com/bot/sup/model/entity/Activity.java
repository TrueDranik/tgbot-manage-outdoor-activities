package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "seasonality")
    private String seasonality;

    @Column(name = "activity_format")
    private String activityFormat;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "description")
    private String description;

    @Column(name = "venue")
    private String venue;       // место проведения

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "duration")
    private LocalDateTime duration;
}
