package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.enums.ActivityEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.enums.ActivityEnum.SCHEDULE;

@RequiredArgsConstructor
@Service
public class CallbackScheduleImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVTIES = Set.of(SCHEDULE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage()
                .getChatId();
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Расписание какой активности Вы хотите настроить?")
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> Buttons = new ArrayList<>();

        InlineKeyboardButton menu =
                InlineKeyboardButton.builder()
                        .text("Меню")
                        .callbackData("MENU")
                        .build();

        Buttons.add(menu);

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("Сап-тур")
                        .callbackData("MваиваENU")
                        .build()
        );
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("байдарка")
                        .callbackData("вив")
                        .build()
        );

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text("Каноэ")
                        .callbackData("jfkd")
                        .build()
        );
        secondRow.add(
                InlineKeyboardButton.builder()
                        .text("Дог-трекинг")
                        .callbackData("fdsa")
                        .build()
        );

        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .keyboardRow(Buttons)
                .build();

        return build;
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVTIES;
    }
}
