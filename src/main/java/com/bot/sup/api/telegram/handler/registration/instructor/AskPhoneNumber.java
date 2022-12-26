package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.MessageService;
import com.bot.sup.validation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskPhoneNumber implements InstructorRegistration {
    private final InstructorDataCache instructorDataCache;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Instructor instructor) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();
        BotApiMethod<?> replyToUser;

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

        return replyToUser;
    }

    @Override
    public boolean support(InstructorStateEnum instructorCurrentState) {
        return InstructorStateEnum.ASK_PHONE_NUMBER.equals(instructorCurrentState);
    }
}
