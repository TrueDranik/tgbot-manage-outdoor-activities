package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CallbackListActivityTypeImpl implements Callback {
    private final ActivityTypeRepository activityTypeRepository;
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.LIST_ACTIVITY_TYPE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttonActivityType = new ArrayList<>();
        List<ActivityType> activityTypes = activityTypeRepository.findAll();

        if (activityTypes.isEmpty()) {
            buttonActivityType.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(mainMessageProperties.getBack())
                            .callbackData("SUP_ACTIVITY_TYPE")
                            .build()));

            InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder()
                    .keyboard(buttonActivityType)
                    .build();

            return EditMessageText.builder()
                    .messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(activityMessageProperties.getEmptyActivity())
                    .replyMarkup(keyboardMarkup)
                    .build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithActivity(activityTypes))
                .text(activityMessageProperties.getListActivityFormat())
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<ActivityType> activityTypes) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> mainRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        activityTypes.forEach(i -> {
            mainRow.add(InlineKeyboardButton.builder()
                    .text(i.getName())
                    .callbackData("ACTIVITY_TYPE_OPTION/" + i.getId())
                    .build());
            if (mainRow.size() == 2) {
                List<InlineKeyboardButton> temporaryKeayboardRow = new ArrayList<>(mainRow);
                mainKeyboard.add(temporaryKeayboardRow);
                mainRow.clear();
            }
        });

        if (mainRow.size() == 1) {
            mainKeyboard.add(mainRow);
        }

        secondRow.add(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData("SUP_ACTIVITY_TYPE")
                .build());
        mainKeyboard.add(secondRow);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
