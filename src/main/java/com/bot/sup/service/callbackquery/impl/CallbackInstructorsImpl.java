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

import static com.bot.sup.enums.ActivityEnum.INSTRUCTORS;

@RequiredArgsConstructor
@Service
public class CallbackInstructorsImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVTIES = Set.of(INSTRUCTORS);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Меню -> Инструкторы")
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Список инструкторов")
                        .callbackData("MваиваENU")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Добавить инструктора")
                        .callbackData("jfkd")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Меню")
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVTIES;
    }
}
