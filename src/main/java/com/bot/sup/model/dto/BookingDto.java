package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private Client clientId;
    private int invitedUsers;
    private Boolean paymentStatus;
}
