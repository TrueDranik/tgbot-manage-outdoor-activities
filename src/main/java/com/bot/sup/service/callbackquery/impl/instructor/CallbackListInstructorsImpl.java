package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.instructor.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackListInstructorsImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorService instructorService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        List<List<InlineKeyboardButton>> buttonEmptyInstructors = new ArrayList<>();
        List<Instructor> instructor = instructorService.findAll();

        if (instructor.isEmpty()) {
            buttonEmptyInstructors.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(mainMessageProperties.getBack())
                            .callbackData(CallbackEnum.INSTRUCTORS.toString())
                            .build()
            ));
            InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                    .keyboard(buttonEmptyInstructors)
                    .build();

            return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(instructorMessageProperties.getEmptyInstructors())
                    .replyMarkup(keyboard)
                    .parseMode(ParseMode.MARKDOWN)
                    .build();
        }

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(generateKeyboardWithInstructors(instructor))
                .text(instructorMessageProperties.getListInstructor())
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(List<Instructor> instructor) {
        List<List<InlineKeyboardButton>> mainKeyboard = new ArrayList<>();
        List<InlineKeyboardButton> rowMain = new ArrayList<>();
        List<InlineKeyboardButton> rowSecond = new ArrayList<>();

        instructor.forEach(i -> {
                    rowMain.add(InlineKeyboardButton.builder()
                            .text(i.getFirstName() + " " + i.getLastName())
                            .callbackData(CallbackEnum.INSTRUCTOR_OPTION + "/" + i.getTelegramId())
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
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.INSTRUCTORS.toString())
                .build());

        mainKeyboard.add(rowSecond);

        return InlineKeyboardMarkup.builder()
                .keyboard(mainKeyboard)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.LIST_INSTRUCTORS;
    }
}
