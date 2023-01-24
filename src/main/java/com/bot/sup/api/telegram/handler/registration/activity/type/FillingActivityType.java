package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.api.telegram.handler.registration.MessageProcessorUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingActivityType implements HandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ActivityTypeStateEnum, ActivityTypeMessageProcessor> activityTypeMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        ActivityType activityType = (ActivityType) userState.getEntity();

        if (userState.getState().equals(ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE)) {
            userState.setState(ActivityTypeStateEnum.START_PROCESSING);
        }

        return processInputMessage(message, chatId, activityType);
    }

    public BotApiMethod<?> processInputMessage(Message message, Long chatId, ActivityType activityType) {
        ActivityTypeStateEnum activityTypeCurrentState = (ActivityTypeStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityTypeMessageProcessorMap.get(activityTypeCurrentState);

        return MessageProcessorUtil.messageProcessorUtil(messageProcessor, message, chatId, activityType);
    }

    @Override
    public Enum<?> getType() {
        return ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
    }
}
