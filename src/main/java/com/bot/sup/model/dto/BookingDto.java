package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private ClientDto client;
    private int invitedUsers;
    private int invitedChildren;
    private String paymentStatus;
    private LocalDate insTime;
    private LocalDate modifTime;
    private Boolean isActive;
    private BigDecimal amountPaid;
    private String paymentType;
    private ScheduleResponseDto scheduleId;
}
