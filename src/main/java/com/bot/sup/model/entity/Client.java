package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String firstName;
    @Column(length = 30)
    private String lastName;
    private LocalDate birthDate;
    @Column(unique = true)
    private Long telegramId;
    private String username;
    private String phoneNumber;
}
