package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import org.springframework.stereotype.Component;

@Component
public interface ActivityTypeMessageProcessor extends MessageProcessor {
    ActivityTypeStateEnum getCurrentState();
}
