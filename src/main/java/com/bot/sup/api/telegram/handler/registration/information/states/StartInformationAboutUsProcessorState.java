package com.bot.sup.api.telegram.handler.registration.information.states;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InformationAboutUsStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartInformationAboutUsProcessorState implements InformationAboutUsMessageProcessor {
    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        userState.setState(InformationAboutUsStateEnum.ASK_FULL_DESCRIPTION);

        return messageService.buildReplyMessage(chatId, "Введите информацию \"О нас\"");
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
    public InformationAboutUsStateEnum getCurrentState() {
        return InformationAboutUsStateEnum.START_PROCESSOR;
    }
}
