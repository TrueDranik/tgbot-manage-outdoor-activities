package com.bot.sup.api.telegram.handler.registration.activity.type;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.activity.type.impl.ActivityTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingActivityType implements HandleRegistration {
    private final ActivityTypeServiceImpl activityTypeServiceImpl;
    private final ActivityTypeRepository activityTypeRepository;
    private final UserStateCache userStateCache;
    private final Map<ActivityTypeStateEnum, ActivityTypeMessageProcessor> activityTypeMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        ActivityType activityType;
        Long chatId = message.getChatId();
        Long activityForUpdateId = 0L;
        UserState userState = userStateCache.getByTelegramId(chatId);

        boolean forUpdate = userState.isForUpdate();
        if (forUpdate) {
            activityType = activityTypeRepository.findById(activityForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found activity type with Id - {} and name - {}", activityType.getId(), activityType.getName());
        } else {
            activityType = (ActivityType) userState.getEntity();
        }

        if (userState.getState().equals(ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE)) {
            userState.setState(ActivityTypeStateEnum.START_PROCESSING);
        }

        return processInputMessage(message, chatId, activityType, forUpdate);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId, ActivityType activityType, boolean forUpdate) {
        ActivityTypeStateEnum activityTypeCurrentState = (ActivityTypeStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityTypeMessageProcessorMap.get(activityTypeCurrentState);

        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        if (forUpdate) {
            activityTypeServiceImpl.save(activityType);
        } else {
            userStateCache.getByTelegramId(chatId).setEntity(activityType);
        }

        return messageProcessor.processInputMessage(message, activityType);
    }

    @Override
    public Enum<?> getType() {
        return ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
    }
}
