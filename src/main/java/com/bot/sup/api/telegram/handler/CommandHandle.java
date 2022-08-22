package com.bot.sup.api.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandle {
    public BotApiMethod<?> sendCommandMessage(Update update);
}
