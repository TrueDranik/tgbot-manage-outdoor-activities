package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class BookingDto {
    private Long id;

    private Client clientId;

    private int invitedUsers;

    private String paymentStatus;
}
