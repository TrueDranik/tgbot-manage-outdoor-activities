package com.bot.sup.api.telegram.handler.registration.instructor.states;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.states.InstructorStateEnum;

public interface InstructorMessageProcessor extends MessageProcessor {
    InstructorStateEnum getCurrentState();
}
