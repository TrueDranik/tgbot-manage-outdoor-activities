package com.bot.sup.api.telegram.handler.registration.instructor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public interface MessageProcessor {
   BotApiMethod<?> processInputMessage(Message message, Object entity);
   BotApiMethod<?> processInvalidInputMessage(Long chatId);
   boolean isMessageInvalid(Message message);
}
