package com.bot.sup.api.telegram.handler.registration.activity.type.states;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.states.ActivityTypeStateEnum;

public interface ActivityTypeMessageProcessor extends MessageProcessor {
    ActivityTypeStateEnum getCurrentState();
}
