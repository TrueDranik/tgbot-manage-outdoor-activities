package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.api.telegram.handler.registration.MessageProcessorUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingActivityFormat implements HandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ActivityFormatStateEnum, ActivityFormatMessageProcessor> activityFormatMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        ActivityFormat activityFormat = (ActivityFormat) userState.getEntity();

        if (userState.getState().equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT)) {
            userState.setState(ActivityFormatStateEnum.START_PROCESSING);
        }

        return processInputMessage(message, chatId, activityFormat);
    }

    public BotApiMethod<?> processInputMessage(Message message, Long chatId, ActivityFormat activityFormat) {
        ActivityFormatStateEnum activityCurrentState = (ActivityFormatStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityFormatMessageProcessorMap.get(activityCurrentState);

        return MessageProcessorUtil.messageProcessorUtil(messageProcessor, message, chatId, activityFormat);
    }

    @Override
    public ActivityFormatStateEnum getType() {
        return ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
    }
}
