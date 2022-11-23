package com.bot.sup.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_time")
    private LocalTime eventTime;

    @Column(name = "participants")
    private Integer participants;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToMany
    @JoinTable(
            name = "schedule_client",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    @JsonManagedReference
    private Set<Client> client;

    @ManyToMany
    @JoinTable(
            name = "schedule_instructor",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    private Set<Instructor> instructor;
}
