package com.bot.sup.api.telegram.handler.registration.information.states;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.states.InformationAboutUsStateEnum;

public interface InformationAboutUsMessageProcessor extends MessageProcessor {
    InformationAboutUsStateEnum getCurrentState();
}
