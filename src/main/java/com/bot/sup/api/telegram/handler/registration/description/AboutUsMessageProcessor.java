package com.bot.sup.api.telegram.handler.registration.description;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.AboutUsStateEnum;

public interface AboutUsMessageProcessor extends MessageProcessor {
    AboutUsStateEnum getCurrentState();
}
