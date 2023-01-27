package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String firstName;
    @Column(length = 30)
    private String lastName;
    @Column( unique = true, length = 30)
    private Long telegramId;
    private String username;
    private String phoneNumber;
}
