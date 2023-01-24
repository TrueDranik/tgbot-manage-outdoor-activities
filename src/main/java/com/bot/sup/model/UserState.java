package com.bot.sup.model;

import lombok.Data;

@Data
public class UserState {
    private Long adminTelegramId;
    private Enum<?> state;
    private Object entity;
    private boolean forUpdate;
}
