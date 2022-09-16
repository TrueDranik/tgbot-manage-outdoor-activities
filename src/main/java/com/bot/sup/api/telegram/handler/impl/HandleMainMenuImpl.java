package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.api.telegram.handler.Handle;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class HandleMainMenuImpl implements Handle {
    @Override
    public BotApiMethod<?> getMessage(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("⬇️Выберите нужное действие⬇️")
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    public InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("\uD83D\uDCC5Расписание\uD83D\uDCC5")
                        .callbackData("SCHEDULE")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("\uD83E\uDDB8Инструкторы\uD83E\uDDB8\u200D♀️")
                        .callbackData("INSTRUCTORS")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("\uD83D\uDEA3\u200D♀️Активности\uD83D\uDEB4\u200D♀️")
                        .callbackData("SAP_ACTIVITY")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}
