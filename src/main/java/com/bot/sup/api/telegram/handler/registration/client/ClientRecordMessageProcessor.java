package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.common.enums.ClientRecordStateEnum;

public interface ClientRecordMessageProcessor extends MessageProcessor {
    ClientRecordStateEnum getCurrentState();
}
