package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.util.MessageProcessorUtil;
import com.bot.sup.api.telegram.handler.registration.client.states.ClientRecordMessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;
import com.bot.sup.common.enums.states.StateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartFillingClientImpl implements ClientHandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ClientRecordStateEnum, ClientRecordMessageProcessor> clientRecordMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        Client client = (Client) userState.getEntity();

        if (userState.getState().equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            userState.setState(ClientRecordStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        ClientRecordStateEnum clientRecordCurrentState = (ClientRecordStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = clientRecordMessageProcessorMap.get(clientRecordCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, client);
    }

    @Override
    public StateEnum<?> getType() {
        return ClientRecordStateEnum.FILLING_CLIENT;
    }
}
