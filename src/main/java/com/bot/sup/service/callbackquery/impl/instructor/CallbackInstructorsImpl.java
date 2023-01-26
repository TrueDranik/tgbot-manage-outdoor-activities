package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackInstructorsImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(instructorMessageProperties.getMenuInstructors())
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(instructorMessageProperties.getListInstructor())
                        .callbackData(CallbackEnum.LIST_INSTRUCTORS.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(instructorMessageProperties.getAddInstructor())
                        .callbackData(CallbackEnum.ADD_INSTRUCTOR.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getMenu())
                        .callbackData(CallbackEnum.MENU.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.INSTRUCTORS;
    }
}
