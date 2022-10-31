package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne
    @JoinColumn(name = "activity_format_id")
    private ActivityFormat activityFormat;

    @OneToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @Column(name = "description", length = 3000)
    private String description;

    @OneToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @Column(name = "duration")
    private String duration;

    @Column(name = "age", length = 10)
    private String age;

    @Column(name = "complexity", length = 30)
    private String complexity;

    @Column(name = "price")
    private String price;
}
