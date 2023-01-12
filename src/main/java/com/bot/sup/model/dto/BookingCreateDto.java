package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingCreateDto {
    private Long id;
    private Client clientId;
    private int invitedUsers;
    private String paymentStatus;
    private LocalDate insTime;
    private LocalDate modifTime;
    private Boolean isActive;
    private BigDecimal amountPaid;
    private String paymentType;
    private Long scheduleId;
}