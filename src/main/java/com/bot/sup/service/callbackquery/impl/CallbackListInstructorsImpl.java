package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
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

import static com.bot.sup.model.common.CallbackEnum.LIST_INSTRUCTORS;

@Service
@RequiredArgsConstructor
public class CallbackListInstructorsImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(LIST_INSTRUCTORS);
    private final InstructorRepository instructorRepository;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> buttonEmptyInstructors = new ArrayList<>();
        List<Instructor> instructor = instructorRepository.findAll();

        if (instructor.size() == 0) {
            buttonEmptyInstructors.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("⬅️Назад")
                            .callbackData("INSTRUCTORS")
                            .build()
            ));
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                    .keyboard(buttonEmptyInstructors)
                    .build();

            return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId()).replyMarkup(keyboard)
                    .text("Инструкторы не найдены!\nВернитесь на главное меню.")
                    .chatId(callbackQuery.getMessage().getChatId()).build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithInstructors(instructor))
                .text("Список инструкторов:")
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(List<Instructor> instructor) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        instructor.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getFirstName() + " " + i.getLastName())
                            .callbackData("INSTRUCTOR_OPTION/" + i.getTelegramId())
                            .build());
                    if (rowMain.size() == 2) {
                        List<InlineKeyboardButton> temporaryKeyboardRow = new ArrayList<>(rowMain);
                        mainKeyboard.add(temporaryKeyboardRow);
                        rowMain.clear();
                    }
                }
        );

        if (rowMain.size() == 1) {
            mainKeyboard.add(rowMain);
        }

        rowSecond.add(InlineKeyboardButton.builder()
                .text("⬅️Назад")
                .callbackData("INSTRUCTORS")
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
