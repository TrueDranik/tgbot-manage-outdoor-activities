package com.bot.sup.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "about_us")
public class AboutUs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_description", length = 1024)
    private String fullDescription;
}
