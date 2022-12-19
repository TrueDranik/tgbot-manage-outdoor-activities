package com.bot.sup.api.telegram.handler;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.common.enums.InstructorStateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bot.sup.common.enums.ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
import static com.bot.sup.common.enums.ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
import static com.bot.sup.common.enums.ClientRecordStateEnum.FILLING_CLIENT;
import static com.bot.sup.common.enums.InstructorStateEnum.FILLING_INSTRUCTOR;

@Component
public class StateContext {
    private final Map<Enum<?>, HandleRegistration> messageHandlers;

    public StateContext(List<HandleRegistration> messageHandlers) {
        this.messageHandlers = messageHandlers.stream()
                .collect(Collectors.toMap(HandleRegistration::getType, Function.identity(),
                        (existing, replacement) -> existing));
    }

    public BotApiMethod<?> processInputMessage(Enum<?> botStateEnum, Message message) {
        HandleRegistration handleRegistration = isFilling(botStateEnum);
        return handleRegistration.getMessage(message);
    }

    private HandleRegistration isFilling(Enum<?> botStateEnum) {
        Set<InstructorStateEnum> stateInstructor = Set.of(InstructorStateEnum.values());
        Set<ActivityFormatStateEnum> stateActivityFormat = Set.of(ActivityFormatStateEnum.values());
        Set<ActivityTypeStateEnum> stateActivityType = Set.of(ActivityTypeStateEnum.values());
        Set<ClientRecordStateEnum> stateClientRecord = Set.of(ClientRecordStateEnum.values());

        if (stateInstructor.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_INSTRUCTOR);
        } else if (stateActivityFormat.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_ACTIVITY_FORMAT);
        } else if (stateActivityType.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_ACTIVITY_TYPE);
        } else if (stateClientRecord.contains(botStateEnum)) {
            return messageHandlers.get(FILLING_CLIENT);
        }
        return messageHandlers.get(botStateEnum);
    }
}
