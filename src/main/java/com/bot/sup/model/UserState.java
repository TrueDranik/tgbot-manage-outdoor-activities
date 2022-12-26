package com.bot.sup.model;

import lombok.Data;

@Data
public class UserState {
    private Long adminTelegramId;
    private String state;
    private Long instructorTelegramId;
}
