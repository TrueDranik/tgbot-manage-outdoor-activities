package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.util.MessageProcessorUtil;
import com.bot.sup.api.telegram.handler.registration.activity.format.states.ActivityFormatMessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityFormatStateEnum;
import com.bot.sup.common.enums.states.StateEnum;
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
public class StartFillingActivityFormat implements ActivityFormatHandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ActivityFormatStateEnum, ActivityFormatMessageProcessor> activityFormatMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();

        UserState userState = userStateCache.getByTelegramId(chatId);
        ActivityFormat activityFormat = (ActivityFormat) userState.getEntity();

        if (userState.getState().equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT)) {
            userState.setState(ActivityFormatStateEnum.START_PROCESSING);
        }

        ActivityFormatStateEnum activityCurrentState = (ActivityFormatStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityFormatMessageProcessorMap.get(activityCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, activityFormat);
    }

    @Override
    public StateEnum<?> getType() {
        return ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
    }
}
