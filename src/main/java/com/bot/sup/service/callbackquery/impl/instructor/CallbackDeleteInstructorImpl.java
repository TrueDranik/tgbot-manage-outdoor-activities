package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CallbackDeleteInstructorImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorRepository instructorRepository;

    public static final CallbackEnum ACTIVITIES = CallbackEnum.DELETE_INSTRUCTOR;

    @Transactional
    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
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
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_INSTRUCTORS.toString())
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
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
