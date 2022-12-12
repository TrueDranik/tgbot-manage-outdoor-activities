package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.ActivityTypeDataCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.activity.type.impl.ActivityTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingActivityType implements HandleRegistration {
    private final MessageService messageService;
    private final ActivityTypeServiceImpl activityTypeServiceImpl;
    private final ActivityTypeDataCache activityTypeDataCache;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityTypeRepository activityTypeRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        ActivityType activityType;
        Long chatId = message.getChatId();
        Long activityForUpdateId = activityTypeDataCache.getActivityTypeForUpdate(chatId);
        boolean forUpdate = activityForUpdateId != null;

        if (forUpdate) {
            activityType = activityTypeRepository.findById(activityForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found activity type with Id - {} and name - {}", activityType.getId(), activityType.getName());
        } else {
            activityType = activityTypeDataCache.getActivityTypeProfileData(chatId);
        }

            if (activityTypeDataCache.getActivityTypeCurrentState(chatId).equals(ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE))
                activityTypeDataCache.setActivityTypeCurrentState(chatId, ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME);

            return processInputMessage(message, chatId, activityType, forUpdate);
        }

        @Transactional
        public BotApiMethod<?> processInputMessage(Message message, Long chatId, ActivityType activityType,
        boolean forUpdate){
            BotApiMethod<?> replyToUser = null;
            String userAnswer = message.getText();
            ActivityTypeStateEnum activityTypeCurrentState = activityTypeDataCache.getActivityTypeCurrentState(chatId);

            if (activityTypeCurrentState.equals(ActivityTypeStateEnum.ASK_ACTIVITY_TYPE_NAME)) {
                replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityFormatName());
                activityTypeDataCache.setActivityTypeCurrentState(chatId, ActivityTypeStateEnum.REGISTERED_ACTIVITY_TYPE);

                return replyToUser;
            } else if (activityTypeCurrentState.equals(ActivityTypeStateEnum.REGISTERED_ACTIVITY_TYPE)) {
                if (activityTypeRepository.existsByNameEqualsIgnoreCase(userAnswer)) {
                    replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
                    activityTypeDataCache.setActivityTypeCurrentState(chatId, ActivityTypeStateEnum.REGISTERED_ACTIVITY_TYPE);

                    return replyToUser;
                }

                try {
                    activityType.setName(userAnswer);
                } catch (IndexOutOfBoundsException e) {
                    return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityNameIsEmpty());
                }


                if (forUpdate) {
                    activityTypeDataCache.removeActivityTypeForUpdate(chatId);
                }else {
                    activityTypeServiceImpl.save(activityType);
                }

                replyToUser = messageService.getReplyMessageWithKeyboard(chatId, activityMessageProperties.getRegisteredActivity(), keyboardMarkup());
            }

            if (forUpdate) {
                activityTypeServiceImpl.save(activityType);
            } else {
                activityTypeDataCache.saveActivityTypeProfileData(chatId, activityType);
            }

            return replyToUser;
        }

        private InlineKeyboardMarkup keyboardMarkup () {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

            buttons.add(List.of(
                    InlineKeyboardButton.builder()
                            .callbackData(CallbackEnum.SUP_ACTIVITY_TYPE.toString())
                            .text(mainMessageProperties.getDone())
                            .build()));
            return InlineKeyboardMarkup.builder()
                    .keyboard(buttons)
                    .build();
        }

        @Override
        public Enum<?> getType () {
            return ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
        }
    }
