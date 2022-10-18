package com.bot.sup.model.dto;

import com.bot.sup.model.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstructorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long telegramId;
    private String username;
    private String phoneNumber;
    private List<Schedule> schedule;
}
