package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.registration.FillingInstructor;
import com.bot.sup.model.common.ActivityEnum;
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

import static com.bot.sup.model.common.ActivityEnum.DELETE_ACTIVITY;

@RequiredArgsConstructor
@Service
public class CallbackDeleteActivityImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(DELETE_ACTIVITY);
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityId = callbackQuery.getData().split("/")[1];
        deleteActivity(Long.parseLong(activityId));

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text("Активность удалена.\nВернитесь в главное меню.")
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteActivity())
                .build();
    }

    private InlineKeyboardMarkup createKeyboardForDeleteActivity() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Меню")
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private void deleteActivity(Long chatId) {
        activityRepository.deleteById(chatId);
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
