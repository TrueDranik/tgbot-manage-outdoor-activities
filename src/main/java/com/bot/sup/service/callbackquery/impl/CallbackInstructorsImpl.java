package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
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

import static com.bot.sup.model.common.CallbackEnum.INSTRUCTORS;

@RequiredArgsConstructor
@Service
public class CallbackInstructorsImpl implements Callback {
    private final MenuMessageProperties menuMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(INSTRUCTORS);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(instructorMessageProperties.getInstructors())
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(instructorMessageProperties.getListInstructor())
                        .callbackData("LIST_INSTRUCTORS")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(instructorMessageProperties.getAddInstructor())
                        .callbackData("ADD_INSTRUCTOR")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(menuMessageProperties.getMenu())
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
