package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingCreateDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int invitedUsers;
    private int invitedChildren;
    private String paymentStatus;
    private BigDecimal amountPaid;
    private String paymentType;
    private Long scheduleId;

}
