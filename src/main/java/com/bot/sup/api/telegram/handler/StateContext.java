package com.bot.sup.api.telegram.handler;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.common.enums.states.StateEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StateContext {
    private final Map<StateEnum<?>, HandleRegistration> messageHandlers;

    public StateContext(List<HandleRegistration> messageHandlers) {
        this.messageHandlers = messageHandlers.stream()
                .collect(Collectors.toMap(HandleRegistration::getType, Function.identity(),
                        (existing, replacement) -> existing));
    }

    public BotApiMethod<?> processInputMessage(Enum<?> botStateEnum, Message message) {
        HandleRegistration handleRegistration = isFilling(botStateEnum);
        return handleRegistration.resolveState(message);
    }

    private HandleRegistration isFilling(Enum<?> botStateEnum) {
        Set<StateEnum<?>> stateEnums = messageHandlers.keySet();
        Optional<StateEnum<?>> first = stateEnums.stream()
                .filter(stateEnum -> stateEnum.getClass().isAssignableFrom(botStateEnum.getClass())).findFirst();

        if (first.isPresent()) {
            return messageHandlers.get(first.get());
        }

        return messageHandlers.get(botStateEnum);
    }
}
