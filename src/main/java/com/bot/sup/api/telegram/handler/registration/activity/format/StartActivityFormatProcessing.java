package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StartActivityFormatProcessing implements ActivityFormatMessageProcessor {
    private final MessageService messageService;
    private final ActivityMessageProperties activityMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object entity) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        userState.setState(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityFormatOrTypeName());
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
    public ActivityFormatStateEnum getCurrentState() {
        return ActivityFormatStateEnum.START_PROCESSING;
    }
}
