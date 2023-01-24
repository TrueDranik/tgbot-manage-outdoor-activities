package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingClientImpl implements HandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ClientRecordStateEnum, ClientRecordMessageProcessor> clientRecordMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        Client client = (Client) userState.getEntity();

        if (userState.getState().equals(ClientRecordStateEnum.FILLING_CLIENT)) {
            userState.setState(ClientRecordStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        return processInputMessage(message, chatId, client);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId, Client client) {
        ClientRecordStateEnum clientRecordCurrentState = (ClientRecordStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = clientRecordMessageProcessorMap.get(clientRecordCurrentState);

        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        return messageProcessor.processInputMessage(message, client);
    }

    @Override
    public Enum<?> getType() {
        return ClientRecordStateEnum.FILLING_CLIENT;
    }
}
