package com.bot.sup.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long telegramId;
    private String username;
    private String phoneNumber;
}
