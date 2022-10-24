package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "event_date_time")
    private LocalDateTime eventDate;

    @Column(name = "participants")
    private Integer participants;

    @ManyToMany
    @JoinColumn(name = "instructor_id")
    private List<Instructor> instructor;

    @ManyToMany
    @JoinColumn(name = "client_id")
    private List<Client> client;
}
