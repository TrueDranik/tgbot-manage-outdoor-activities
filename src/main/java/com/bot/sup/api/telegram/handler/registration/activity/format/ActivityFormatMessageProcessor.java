package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import org.springframework.stereotype.Component;

@Component
public interface ActivityFormatMessageProcessor extends MessageProcessor {
    ActivityFormatStateEnum getCurrentState();
}
