package com.bot.sup.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Client client;
    private int invitedUsers;
    private int invitedChildren;
    private String paymentStatus;
    @CreationTimestamp
    private LocalDate insTime;
    @LastModifiedDate
    private LocalDate modifTime;
    private boolean isActive = true;
    private BigDecimal amountPaid;
    private String paymentType;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}