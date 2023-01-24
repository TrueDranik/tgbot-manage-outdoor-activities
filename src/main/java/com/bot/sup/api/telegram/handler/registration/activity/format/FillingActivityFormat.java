package com.bot.sup.api.telegram.handler.registration.activity.format;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
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
public class FillingActivityFormat implements HandleRegistration {
    private final ActivityFormatRepository activityFormatRepository;
    private final UserStateCache userStateCache;
    private final Map<ActivityFormatStateEnum, ActivityFormatMessageProcessor> activityFormatMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        ActivityFormat activityFormat;
        Long chatId = message.getChatId();
        Long activityFormatForUpdateId = 0L;
        UserState userState = userStateCache.getByTelegramId(chatId);

        boolean forUpdate = userState.isForUpdate();
        if (forUpdate) {
            activityFormat = activityFormatRepository.findById(activityFormatForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found activity form with Id - {} and name - {}", activityFormat.getId(), activityFormat.getName());
        } else {
            activityFormat = (ActivityFormat) userState.getEntity();
        }

        if (userState.getState().equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT)) {
            userState.setState(ActivityFormatStateEnum.START_PROCESSING);
        }

        return processInputMessage(message, chatId, activityFormat, forUpdate);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message message, Long chatId, ActivityFormat activityFormat, boolean forUpdate) {
        ActivityFormatStateEnum activityCurrentState = (ActivityFormatStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = activityFormatMessageProcessorMap.get(activityCurrentState);

        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        if (forUpdate) {
            activityFormatRepository.save(activityFormat);
        } else {
            userStateCache.getByTelegramId(chatId).setEntity(activityFormat);
        }

        return messageProcessor.processInputMessage(message, activityFormat);
    }

    @Override
    public ActivityFormatStateEnum getType() {
        return ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
    }
}
