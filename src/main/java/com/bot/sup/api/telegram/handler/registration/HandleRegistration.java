package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.common.enums.states.StateEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface HandleRegistration {
    BotApiMethod<?> resolveState(Message message);

    StateEnum<?> getType();
}
