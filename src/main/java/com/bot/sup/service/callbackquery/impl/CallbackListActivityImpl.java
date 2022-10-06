package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
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

import static com.bot.sup.model.common.CallbackEnum.LIST_ACTIVITY;

@Service
@RequiredArgsConstructor
public class CallbackListActivityImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(LIST_ACTIVITY);
    private final ActivityRepository activityRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> buttonEmptyInstructors = new ArrayList<>();
        List<Activity> activities = activityRepository.findAll();

        if (activities.isEmpty()) {
            buttonEmptyInstructors.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("⬅️ Назад")
                            .callbackData("SUP_ACTIVITY")
                            .build()
            ));
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                    .keyboard(buttonEmptyInstructors)
                    .build();

            return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId()).replyMarkup(keyboard)
                    .text("Активности не найдены.\nВернитесь на главное меню")
                    .chatId(callbackQuery.getMessage().getChatId()).build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithActivity(activities))
                .text("Список активностей")
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(List<Activity> activities) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        activities.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getName())
                            .callbackData("ACTIVITY_OPTION/" + i.getId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text("⬅️ Назад")
                .callbackData("SUP_ACTIVITY")
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
