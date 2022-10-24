package com.bot.sup.api.telegram.handler;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.model.common.ActivityFormatStateEnum;
import com.bot.sup.model.common.ActivityTypeStateEnum;
import com.bot.sup.model.common.InstructorStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bot.sup.model.common.ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
import static com.bot.sup.model.common.ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
import static com.bot.sup.model.common.InstructorStateEnum.FILLING_INSTRUCTOR;

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
        Set<ActivityFormatStateEnum> stateActivityFormat = Set.of(ActivityFormatStateEnum.values());
        Set<ActivityTypeStateEnum> stateActivityType = Set.of(ActivityTypeStateEnum.values());

        if (stateInstructor.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_INSTRUCTOR);
        } else if (stateActivityFormat.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_ACTIVITY_FORMAT);
        } else if (stateActivityType.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_ACTIVITY_TYPE);
        }
        return messageHandlers.get(botStateEnum);
    }
}
