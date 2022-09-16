package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.api.telegram.handler.InstructorStateContext;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public interface HandleRegistration {
    BotApiMethod<?> getMessage(Message message);

    RegistrationInstructorStateEnum getFullName();
}
