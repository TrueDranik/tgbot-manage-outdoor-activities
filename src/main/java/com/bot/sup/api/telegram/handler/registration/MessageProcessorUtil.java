package com.bot.sup.api.telegram.handler.registration;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@UtilityClass
public class MessageProcessorUtil {
    public static BotApiMethod<?> messageProcessorUtil(MessageProcessor messageProcessor, Message message, Long chatId, Object entity) {
        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        return messageProcessor.processInputMessage(message, entity);
    }
}
