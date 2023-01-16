package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private Client clientId;
    private int invitedUsers;
    private int invitedChildren;
    private String paymentStatus;
    private LocalDate insTime;
    private LocalDate modifTime;
    private Boolean isActive;
    private BigDecimal amountPaid;
    private String paymentType;
    private Long scheduleId;
}
