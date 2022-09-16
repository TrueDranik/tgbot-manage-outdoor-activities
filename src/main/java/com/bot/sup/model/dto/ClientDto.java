package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientDto {
    private Long id;

    private String firstName;

    private String secondName;

    private long tgId;

    private String phoneNumber;

    private LocalDate birthDate;
}
