package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.InstructorStateEnum;

public interface InstructorMessageProcessor extends MessageProcessor {
    InstructorStateEnum getCurrentState();
}
