package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.model.dto.InstructorDto;
import com.bot.sup.service.InstructorService;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class FillingInstructor implements HandleRegistration {
    private final InstructorDataCache instructorDataCache;
    private final MessageService messageService;
    private final InstructorService instructorService;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        if (instructorDataCache.getInstructorCurrentState(chatId).equals(RegistrationInstructorStateEnum.FILLING_PROFILE))
            instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.ASK_FULL_NAME);
        return processInputMessage(message);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage) {
        Long chatId = inputMessage.getChatId();
        String userAnswer = inputMessage.getText();
        InstructorDto instructorDto = instructorDataCache.getInstructorProfileData(chatId);
        RegistrationInstructorStateEnum instructorCurrentState = instructorDataCache.getInstructorCurrentState(chatId);
        BotApiMethod<?> replyToUser = null;

        if (instructorCurrentState.equals(RegistrationInstructorStateEnum.ASK_FULL_NAME)) {
            instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.ASK_PHONE_NUMBER);
            replyToUser = messageService.buildReplyMessage(chatId, "Введи ниже имя и фамилию (через пробел).");
        }else if (instructorCurrentState.equals(RegistrationInstructorStateEnum.ASK_PHONE_NUMBER)) {
            if (Validation.isValidText(userAnswer)) {
                try {
                    String[] fullName = userAnswer.split(" ");

                    instructorDto.setFirstName(fullName[0]);
                    instructorDto.setSecondName(fullName[1]);

                    if (instructorDto.getFirstName().length() < 2 || instructorDto.getSecondName().length() > 15
                            && instructorDto.getSecondName().length() < 2 || instructorDto.getSecondName().length() > 15) {
                        return messageService.buildReplyMessage(chatId, "Имя и фамилия может быть от 2 до 15 символов!");
                    }
                } catch (IndexOutOfBoundsException e) {
                    replyToUser = messageService.buildReplyMessage(chatId, "Вы не ввели ФИ!");
                    return replyToUser;
                }

                log.info("instructor Name = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId, "Введите номер телефона.");

                instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.ASK_TELEGRAM_ID);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "Допустимы только кириллица и английский!");
                instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.ASK_PHONE_NUMBER);

                return replyToUser;
            }
        } else if (instructorCurrentState.equals(RegistrationInstructorStateEnum.ASK_TELEGRAM_ID)) {
            if (Validation.isValidPhoneNumber(userAnswer)) {
                instructorDto.setPhoneNumber(userAnswer);

                log.info("instructor phone number = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId,
                        "Перешлите любое сообщение инструктора для получегия его telegramId");

                instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.REGISTERED);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, "Неверный формат номера!");
                instructorDataCache.setInstructorCurrentState(chatId, RegistrationInstructorStateEnum.ASK_TELEGRAM_ID);

                return replyToUser;
            }
        } else if (instructorCurrentState.equals(RegistrationInstructorStateEnum.REGISTERED)) {
            instructorDto.setTgId(inputMessage.getForwardFrom().getId());

            instructorService.save(instructorDto);
            log.info("data instructor save in db");

            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "Инструктор зарегистрирован!", keyboardMenu());
        }

        instructorDataCache.saveInstructorProfileData(chatId, instructorDto);

        return replyToUser;
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder().callbackData("INSTRUCTORS").text("Инструкторы").build()
        ));

        return InlineKeyboardMarkup.builder().keyboard(buttons).build();
    }

    @Override
    public RegistrationInstructorStateEnum getFullName() {
        return RegistrationInstructorStateEnum.FILLING_PROFILE;
    }
}
