package com.bot.sup.model.entity;

import jdk.jfr.BooleanFlag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "ins_time")
    private LocalDate insTime;

    @Column(name = "modif_time")
    private LocalDate modifTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "amount_paid")
    private BigDecimal amount_paid;

    @Column(name = "payment_type")
    private String paymentType;
}
