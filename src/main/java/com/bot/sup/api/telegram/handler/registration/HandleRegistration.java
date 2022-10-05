package com.bot.sup.api.telegram.handler.registration;

//import com.bot.sup.model.common.BotStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public interface HandleRegistration {
    BotApiMethod<?> getMessage(Message message);

    Enum<?> getType();
}
