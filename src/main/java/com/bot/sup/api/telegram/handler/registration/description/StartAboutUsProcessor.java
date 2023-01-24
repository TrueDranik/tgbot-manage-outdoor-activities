package com.bot.sup.api.telegram.handler.registration.description;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartAboutUsProcessor implements AboutUsMessageProcessor{
    private final MessageService messageService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        userState.setState(AboutUsStateEnum.ASK_FULL_DESCRIPTION);

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
    public AboutUsStateEnum getCurrentState() {
        return AboutUsStateEnum.START_PROCESSOR;
    }
}
