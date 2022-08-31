package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.enums.ActivityEnum;
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

import static com.bot.sup.enums.ActivityEnum.DELETE_INSTRUCTOR;

@RequiredArgsConstructor
@Service
public class CallbackDeleteInstructorImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(DELETE_INSTRUCTOR);
    private final InstructorRepository instructorRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        deleteInstructor(Long.parseLong(instructorId));

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text("Инструктор удален.\nВернитесь списку инструкторов.")
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteInstructor())
                .build();
    }

    private InlineKeyboardMarkup createKeyboardForDeleteInstructor() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("⬅️Назад")
                        .callbackData("LIST_INSTRUCTORS")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private void deleteInstructor(long chatId) { instructorRepository.deleteById(chatId); }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {return ACTIVITIES;}
}
