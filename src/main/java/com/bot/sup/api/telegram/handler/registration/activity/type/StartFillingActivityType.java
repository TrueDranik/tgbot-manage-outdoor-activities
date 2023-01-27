package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.util.MessageProcessorUtil;
import com.bot.sup.api.telegram.handler.registration.activity.type.states.ActivityTypeMessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityTypeStateEnum;
import com.bot.sup.common.enums.states.StateEnum;
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
public class StartFillingActivityType implements ActivityTypeHandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<ActivityTypeStateEnum, ActivityTypeMessageProcessor> activityTypeMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        ActivityType activityType = (ActivityType) userState.getEntity();

        if (userState.getState().equals(ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE)) {
            userState.setState(ActivityTypeStateEnum.START_PROCESSING);
        }

        ActivityTypeStateEnum activityTypeCurrentState = (ActivityTypeStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityTypeMessageProcessorMap.get(activityTypeCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, activityType);
    }

    @Override
    public StateEnum<?> getType() {
        return ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
    }
}
