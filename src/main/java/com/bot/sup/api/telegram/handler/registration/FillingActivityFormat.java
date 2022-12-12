package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.ActivityFormatDataCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.activity.format.impl.ActivityFormatServiceImpl;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingActivityFormat implements HandleRegistration {
    private final MessageService messageService;
    private final ActivityFormatServiceImpl activityFormatServiceImpl;
    private final ActivityFormatDataCache activityFormatDataCache;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        ActivityFormat activityFormat;
        Long chatId = message.getChatId();
        Long activityFormatForUpdateId = activityFormatDataCache.getActivityFormatForUpdate(chatId);
        boolean forUpdate = activityFormatForUpdateId != null;

        if (forUpdate) {
            activityFormat = activityFormatRepository.findById(activityFormatForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found activity form with Id - {} and name - {}", activityFormat.getId(), activityFormat.getName());
        } else {
            activityFormat = activityFormatDataCache.getActivityFormatProfileData(chatId);
        }


        if (activityFormatDataCache.getActivityFormatCurrentState(chatId).equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT))
            activityFormatDataCache.setActivityFormatCurrentState(chatId, ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return processInputMessage(message, chatId, activityFormat, forUpdate);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId, ActivityFormat activityFormat, boolean forUpdate) {
        BotApiMethod<?> replyToUser = null;
        String userAnswer = inputMessage.getText();
        ActivityFormatStateEnum activityCurrentState = activityFormatDataCache.getActivityFormatCurrentState(chatId);

        if (activityCurrentState.equals(ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME)) {
            replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityFormatName());
            activityFormatDataCache.setActivityFormatCurrentState(chatId, ActivityFormatStateEnum.REGISTERED_ACTIVITY_FORMAT);

            return replyToUser;
        } else if (activityCurrentState.equals(ActivityFormatStateEnum.REGISTERED_ACTIVITY_FORMAT)) {
            if (activityFormatRepository.existsByNameEqualsIgnoreCase(userAnswer)) {
                replyToUser = messageService.buildReplyMessage(chatId, activityMessageProperties.getActivityNameAlreadyTaken());
                activityFormatDataCache.setActivityFormatCurrentState(chatId, ActivityFormatStateEnum.REGISTERED_ACTIVITY_FORMAT);

                return replyToUser;
            }
            try {
                activityFormat.setName(userAnswer);
            } catch (IndexOutOfBoundsException e) {
                return messageService.buildReplyMessage(chatId, activityMessageProperties.getInputActivityNameIsEmpty());
            }

            if (forUpdate) {
                activityFormatDataCache.removeActivityFormatForUpdate(chatId);
            } else {
                activityFormatServiceImpl.save(activityFormat);
            }


            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, String
                            .format(activityMessageProperties.getRegisteredActivity(), activityFormat.getName()),
                    keyboardMenu());
        }

        if (forUpdate) {
            activityFormatServiceImpl.save(activityFormat);
        } else {
            activityFormatDataCache.saveActivityFormatProfileData(chatId, activityFormat);
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
                        .text(mainMessageProperties.getDone())
                        .build()
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public ActivityFormatStateEnum getType() {
        return ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
    }
}
