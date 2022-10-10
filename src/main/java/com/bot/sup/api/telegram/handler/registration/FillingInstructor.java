package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.model.common.InstructorStateEnum;
import com.bot.sup.model.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.InstructorService;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingInstructor implements HandleRegistration {
    private final InstructorDataCache instructorDataCache;
    private final MessageService messageService;
    private final InstructorService instructorService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorRepository instructorRepository;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Instructor instructor;
        Long chatId = message.getChatId();
        Long instructorForUpdateId = instructorDataCache.getInstructorForUpdate(chatId);
        boolean forUpdate = instructorForUpdateId != null;

        if (forUpdate) {
            instructor = instructorRepository.findByTelegramId(instructorForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found instructor with tgId - {} and name - {}", instructor.getTelegramId(), instructor.getFirstName());
        } else {
            instructor = instructorDataCache.getInstructorProfileData(chatId);
        }
        if (instructorDataCache.getInstructorCurrentState(chatId).equals(InstructorStateEnum.FILLING_INSTRUCTOR))
            instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_FULL_NAME);

        return processInputMessage(message, chatId, instructor, forUpdate);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId, Instructor instructor, boolean forUpdate) {
        String userAnswer = inputMessage.getText();
        InstructorStateEnum instructorCurrentState = instructorDataCache.getInstructorCurrentState(chatId);
        BotApiMethod<?> replyToUser = null;

        if (instructorCurrentState.equals(InstructorStateEnum.ASK_FULL_NAME)) {
            replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructor());
            instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_PHONE_NUMBER);

            return replyToUser;
        } else if (instructorCurrentState.equals(InstructorStateEnum.ASK_PHONE_NUMBER)) {
            if (Validation.isValidText(userAnswer)) {
                try {
                    String[] fullName = userAnswer.split(" ");

                    instructor.setFirstName(fullName[0]);
                    instructor.setLastName(fullName[1]);

                    if (instructor.getFirstName().length() < 2 || instructor.getFirstName().length() > 15
                            && instructor.getLastName().length() < 2 || instructor.getLastName().length() > 15) {
                        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getValidateInputFullName());
                    }
                } catch (IndexOutOfBoundsException e) {
                    return messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructorIsEmpty());
                }

                log.info("instructor Name = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputPhoneNumber());

                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_TELEGRAM_ID);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getValidateLanguage());
                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_PHONE_NUMBER);

                return replyToUser;
            }
        } else if (instructorCurrentState.equals(InstructorStateEnum.ASK_TELEGRAM_ID)) {
            if (Validation.isValidPhoneNumber(userAnswer)) {
                instructor.setPhoneNumber(userAnswer);

                log.info("instructor phone number = " + userAnswer);

                replyToUser = messageService.buildReplyMessage(chatId,
                        instructorMessageProperties.getGetTelegramId());

                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.REGISTERED_INSTRUCTOR);
            } else {
                replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getPhoneNumberNotValid());
                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_TELEGRAM_ID);

                return replyToUser;
            }
        } else if (instructorCurrentState.equals(InstructorStateEnum.REGISTERED_INSTRUCTOR)) {
            Optional<User> forwardFrom = Optional.ofNullable(inputMessage.getForwardFrom());

            if (forwardFrom.isPresent() && instructorRepository.existsByTelegramId(forwardFrom.get().getId()) && !forUpdate) {
                replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getTelegramIdAlreadyTaken());
                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.REGISTERED_INSTRUCTOR);

                return replyToUser;
            } else if (forwardFrom.isEmpty()) {
                replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getTelegramIdIsEmpty());
                instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.REGISTERED_INSTRUCTOR);

                return replyToUser;
            }

            instructor.setTelegramId(inputMessage.getForwardFrom().getId());

            if (forUpdate) {
                instructorDataCache.removeInstructorForUpdate(chatId);
            } else {
                instructorService.save(instructor);
            }

            replyToUser = messageService.getReplyMessageWithKeyboard(chatId, "Инструктор зарегистрирован!\n" +
                    instructorInfo(instructor), keyboardMenu());
        }

        if (forUpdate) {
            instructorService.save(instructor);
        } else {
            instructorDataCache.saveInstructorProfileData(chatId, instructor);
        }

        return replyToUser;
    }

    private String instructorInfo(Instructor instructor) {
        return "ФИ: " + instructor.getFirstName() + " " + instructor.getLastName()
                + "\nНомер телефона: " + instructor.getPhoneNumber()
                + "\nTelegramId: " + instructor.getTelegramId();
    }

    private InlineKeyboardMarkup keyboardMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .callbackData("INSTRUCTORS")
                        .text(instructorMessageProperties.getInstructors())
                        .build()
        ));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public InstructorStateEnum getType() {
        return InstructorStateEnum.FILLING_INSTRUCTOR;
    }
}
