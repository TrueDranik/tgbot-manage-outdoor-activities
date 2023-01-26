package com.bot.sup.api.telegram.handler.registration.client;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ClientRecordStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartClientProcessorRecord implements ClientRecordMessageProcessor {
    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object client) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        userState.setState(ClientRecordStateEnum.ASK_TELEGRAM_ID);

        return messageService.buildReplyMessage(chatId, "Перешлите любое сообщение клиента");
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return null;
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return false;
    }

    @Override
    public ClientRecordStateEnum getCurrentState() {
        return ClientRecordStateEnum.START_PROCESSING;
    }
}
