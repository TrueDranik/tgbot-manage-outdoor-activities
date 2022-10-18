package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "activity")
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seasonality")
    private String seasonality;

    @OneToOne
    @JoinColumn(name = "activity_form_id")
    private ActivityForm activityForm;

    @OneToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @Column(name = "description", length = 3000)
    private String description;

    @OneToOne
    @JoinColumn(name = "route_id")
    private Activity route;

    @Column(name = "duration")
    private LocalTime duration;

    @Column(name = "age", length = 10)
    private String age;

    @Column(name = "complexity", length = 30)
    private String complexity;

    @Column(name = "price")
    private BigDecimal price;
}
