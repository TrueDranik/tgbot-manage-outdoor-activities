package com.bot.sup.api.telegram.handler;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.model.common.InstructorStateEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bot.sup.model.common.InstructorStateEnum.FILLING_INSTRUCTOR;
import static com.bot.sup.model.common.SupActivityStateEnum.FILLING_ACTIVITY;

@Component
public class StateContext {
    private final Map<Enum<?>, HandleRegistration> messageHandlers = new HashMap<>();

    public StateContext(List<HandleRegistration> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getType(), handler));
    }

    public BotApiMethod<?> processInputMessage(Enum<?> botStateEnum, Message message) {
        HandleRegistration handleRegistration = isFilling(botStateEnum);
        return handleRegistration.getMessage(message);
    }

    private HandleRegistration isFilling(Enum<?> botStateEnum) {
        Set<InstructorStateEnum> stateInstructor = Set.of(InstructorStateEnum.values());
        Set<SupActivityStateEnum> stateActivity = Set.of(SupActivityStateEnum.values());

        if (stateInstructor.contains(botStateEnum))
            return messageHandlers.get(FILLING_INSTRUCTOR);
        else if (stateActivity.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_ACTIVITY);
        }

        return messageHandlers.get(botStateEnum);
    }
}
