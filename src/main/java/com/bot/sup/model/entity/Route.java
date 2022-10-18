package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "activity")
@Getter
@Setter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "start_point_coordinates", length = 300)
    private String startPointCoordinates;

    @Column(name = "start_point_name", length = 30)
    private String startPointName;

    @Column(name = "finish_point_coordinates", length = 300)
    private String finishPointCoordinates;

    @Column(name = "finish_point_name", length = 30)
    private String finishPointName;

    @Column(name = "map_link", length = 300)
    private String mapLink;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Activity activity;
}
