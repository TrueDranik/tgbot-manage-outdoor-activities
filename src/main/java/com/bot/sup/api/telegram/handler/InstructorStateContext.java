package com.bot.sup.api.telegram.handler;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InstructorStateContext {
    private final Map<RegistrationInstructorStateEnum, HandleRegistration> messageHandlers = new HashMap<>();

    public InstructorStateContext(List<HandleRegistration> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getFullName(), handler));
    }

    public BotApiMethod<?> processInputMessage(RegistrationInstructorStateEnum instructorState, Message message)  {
        HandleRegistration handleRegistration = findMessageHandler(instructorState);
        return handleRegistration.getMessage(message);
    }

    private HandleRegistration findMessageHandler(RegistrationInstructorStateEnum masterBotState) {
        if (isFillingProfileState(masterBotState)) {
            return messageHandlers.get(RegistrationInstructorStateEnum.FILLING_PROFILE);
        }
        return messageHandlers.get(masterBotState);
    }

    private boolean isFillingProfileState(RegistrationInstructorStateEnum instructorState) {
        return switch (instructorState) {
            case ASK_FULL_NAME, ASK_PHONE_NUMBER, ASK_TELEGRAM_ID, REGISTERED -> true;
            default -> false;
        };
    }
}
