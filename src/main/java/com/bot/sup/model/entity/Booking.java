package com.bot.sup.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jdk.jfr.BooleanFlag;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Entity
@Table(name = "booking")
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Client clientId;

    @Column(name= "invited_users")
    private int invitedUsers;

    @Column(name= "invited_children")
    private int invitedChildren;

    @Column(name= "payment_status")
    private String paymentStatus;

    @Column(name= "ins_time")
    @CreationTimestamp
    private LocalDate insTime;

    @Column(name= "modif_time")
    @LastModifiedDate
    private LocalDate modifTime;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "payment_type")
    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
}