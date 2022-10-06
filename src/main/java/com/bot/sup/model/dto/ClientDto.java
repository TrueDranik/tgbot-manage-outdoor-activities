package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientDto {
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Long telegramId;

    private String phoneNumber;
}
