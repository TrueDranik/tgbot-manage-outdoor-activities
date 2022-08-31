package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.enums.ActivityEnum;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.enums.ActivityEnum.INSTRUCTOR_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackInstructorsOptionImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(INSTRUCTOR_OPTION);
    private final InstructorRepository instructorRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        Optional<Instructor> instructor = instructorRepository.findById(Long.parseLong(instructorId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(instructor.get().getFirstName() + " " +instructor.get().getSecondName())
                .replyMarkup(generateKeyboardWithInstructors(instructorId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(String instructorId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("\uD83D\uDD04Изменить")
                        .callbackData("ACTIVITY_CHANGE")
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text("❌Удалить")
                        .callbackData("DELETE_INSTRUCTOR/" + instructorId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text("⬅️Назад")
                        .callbackData("LIST_INSTRUCTORS")
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
