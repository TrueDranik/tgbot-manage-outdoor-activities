package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.ActivityFormatDataCache;
import com.bot.sup.model.common.ActivityFormatStateEnum;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.activityFormat.impl.ActivityFormatServiceImpl;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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
        Long chatId = message.getChatId();

        if (activityFormatDataCache.getActivityFormatCurrentState(chatId).equals(ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT))
            activityFormatDataCache.setActivityFormatCurrentState(chatId, ActivityFormatStateEnum.ASK_ACTIVITY_FORMAT_NAME);

        return processInputMessage(message, chatId);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId) {
        BotApiMethod<?> replyToUser = null;
        ActivityFormat activityFormat = new ActivityFormat();
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

            activityFormatServiceImpl.save(activityFormat);

            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, String
                            .format(activityMessageProperties.getRegisteredActivity(), activityFormat.getName()),
                    keyboardMenu());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData("SUP_ACTIVITY_FORMAT")
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
