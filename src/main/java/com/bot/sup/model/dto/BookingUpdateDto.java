package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookingUpdateDto {
    private Long id;
    private ClientDto clientId;
    private int invitedUsers;
    private int invitedChildren;
    private String paymentStatus;
    private BigDecimal amountPaid;
    private String paymentType;
    private Long scheduleId;
    private LocalDate insTime;
}
