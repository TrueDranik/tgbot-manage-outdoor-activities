package com.bot.sup.model.entity;

import jdk.jfr.BooleanFlag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "invited_users")
    private int invitedUsers;

    @BooleanFlag
    @Column(name = "payment_status")
    private Boolean paymentStatus;
}
