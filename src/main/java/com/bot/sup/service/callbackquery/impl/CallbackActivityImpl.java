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

import static com.bot.sup.enums.ActivityEnum.SAP_ACTIVITY;

@RequiredArgsConstructor
@Service
public class CallbackActivityImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(SAP_ACTIVITY);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text("Меню -> Активности")
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("\uD83D\uDCDDСписок активностей")
                        .callbackData("LIST_ACTIVITY")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("\uD83C\uDD95Добавить активность")
                        .callbackData("ADD_ACTIVITY")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text("↖️Меню↖️")
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
