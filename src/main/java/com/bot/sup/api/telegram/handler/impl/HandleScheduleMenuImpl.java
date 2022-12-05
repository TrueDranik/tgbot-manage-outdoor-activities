package com.bot.sup.api.telegram.handler.impl;

import com.bot.sup.api.telegram.handler.Handle;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HandleScheduleMenuImpl implements Handle {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    @Override
    public BotApiMethod<?> getMessage(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(scheduleMessageProperties.getMenuSchedules())
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(activityMessageProperties.getListActivityFormat())
                .callbackData(CallbackEnum.SCHEDULE_TO_ACTIVITYFORMAT.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getTourEditor())
                .callbackData(CallbackEnum.SCHEDULE_WEBAPP.toString())
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getMenu())
                .callbackData(CallbackEnum.MENU.toString())
                .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }
}
