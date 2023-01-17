package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "telegram_id", unique = true)
    private Long telegramId;

    @Column(name = "username")
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "schedule_client",
//            joinColumns = @JoinColumn(name = "client_id"),
//            inverseJoinColumns = @JoinColumn(name = "schedule_id")
//    )
//    @JsonBackReference
//    private Set<Schedule> schedule;
}
