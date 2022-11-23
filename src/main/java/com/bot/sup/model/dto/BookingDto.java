package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Client;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {
    private Long id;
    private Client clientId;
    private int invitedUsers;
    private Boolean paymentStatus;
    private LocalDate insTime;
    private LocalDate modifTime;

}
