package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.model.common.CallbackEnum.DELETE_INSTRUCTOR;

@RequiredArgsConstructor
@Service
public class CallbackDeleteInstructorImpl implements Callback {
    private final MenuMessageProperties menuMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorRepository instructorRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(DELETE_INSTRUCTOR);

    @Transactional
    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        deleteInstructor(Long.parseLong(instructorId));

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text(instructorMessageProperties.getDeleteInstructor())
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteInstructor())
                .build();
    }


    private InlineKeyboardMarkup createKeyboardForDeleteInstructor() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(menuMessageProperties.getBack())
                        .callbackData("LIST_INSTRUCTORS")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Transactional
    public void deleteInstructor(Long instructorId) {
        instructorRepository.deleteByTelegramId(instructorId);
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
