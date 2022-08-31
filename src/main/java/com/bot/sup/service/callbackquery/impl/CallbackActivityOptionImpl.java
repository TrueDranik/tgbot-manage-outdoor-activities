package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.enums.ActivityEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.repository.ActivityRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.enums.ActivityEnum.ACTIVITY_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackActivityOptionImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVTIES = Set.of(ACTIVITY_OPTION);
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityId = callbackQuery.getData().split("/")[1];
        Optional<Activity> activity = activityRepository.findById(Long.parseLong(activityId));
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activity.get().getName())
                .replyMarkup(generateKeyboardWithActivity(activityId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(String activityId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("Изменить")
                        .callbackData("ACTIVITY_CHANGE")
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("Удалить")
                        .callbackData("DELETE_ACTIVITY/" + activityId)
                        .build());


        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVTIES;
    }
}
