package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.common.enums.states.StateEnum;

public interface InstructorHandleRegistration<T extends Enum<T>> extends HandleRegistration {
        StateEnum<?> getType();
}
