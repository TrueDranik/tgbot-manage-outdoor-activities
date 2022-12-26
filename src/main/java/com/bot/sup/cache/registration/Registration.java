package com.bot.sup.cache.registration;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

//@Component
public interface Registration {
    PartialBotApiMethod<?> handleMessage(Message message);

    Enum<?> getState();
}
