package com.bot.sup.api.telegram.handler.registration.client.states;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;

public interface ClientRecordMessageProcessor extends MessageProcessor {
    ClientRecordStateEnum getCurrentState();
}
