package com.bot.sup.api.telegram.handler.registration;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageProcessor {
    BotApiMethod<?> processInputMessage(Message message, Object entity);

    BotApiMethod<?> processInvalidInputMessage(Long chatId);

    boolean isMessageInvalid(Message message);
}
