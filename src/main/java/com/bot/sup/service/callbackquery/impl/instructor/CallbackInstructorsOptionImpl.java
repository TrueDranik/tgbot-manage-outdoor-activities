package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.INSTRUCTOR_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackInstructorsOptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final InstructorRepository instructorRepository;

    public static final CallbackEnum ACTIVITIES = INSTRUCTOR_OPTION;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        Optional<Instructor> instructor = Optional.ofNullable(instructorRepository.findByTelegramId(Long.parseLong(instructorId))
                .orElseThrow(() -> new EntityNotFoundException("Instructor with id[" + instructorId + "] not found")));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(instructorInfo(instructor))
                .parseMode("HTML")
                .replyMarkup(generateKeyboardWithInstructors(instructorId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithInstructors(String instructorId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.CHANGE_INSTRUCTOR + "/" + instructorId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_INSTRUCTOR + "/" + instructorId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_INSTRUCTORS.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    private String instructorInfo(Optional<Instructor> instructor) {
        // todo вынести в ресурсы
        String userId = String.format("<a href=\"tg://user?id=%s\"> (профиль)</a>", instructor.get().getTelegramId().toString());
        return "\uD83E\uDEAA ФИ: " + instructor.get().getFirstName() + " " + instructor.get().getLastName() + userId
                + "\n☎️ Номер телефона: " + instructor.get().getPhoneNumber()
                + "\n\uD83C\uDF10 Имя пользователя: @" + instructor.map(Instructor::getUsername).orElse("");
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
