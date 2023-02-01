package com.bot.sup.api.telegram.handler.registration.activity.format.states;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.states.ActivityFormatStateEnum;

public interface ActivityFormatMessageProcessor extends MessageProcessor {
    ActivityFormatStateEnum getCurrentState();
}
