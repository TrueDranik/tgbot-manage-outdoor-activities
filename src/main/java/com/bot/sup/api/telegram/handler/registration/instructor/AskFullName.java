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
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskFullName implements InstructorMessageProcessor {
    private final InstructorDataCache instructorDataCache;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private String errorMessage;
    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();
        BotApiMethod<?> replyToUser;
        String[] fullName = userAnswer.split(" ");

        ((Instructor) instructor).setFirstName(fullName[0]);
        ((Instructor) instructor).setLastName(fullName[1]);

        log.info("instructor Name = " + userAnswer);

        replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputPhoneNumber());
        instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_PHONE_NUMBER);

        return replyToUser;
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_FULL_NAME);
        return messageService.buildReplyMessage(chatId, errorMessage);
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        boolean validText = !Validation.isValidText(message.getText());
        boolean validFio = StringUtils.hasText(message.getText()) && message.getText().split(" ").length < 2;
        if (!validText) {
           errorMessage = instructorMessageProperties.getValidateLanguage();
        } else if (validFio) {
            errorMessage = instructorMessageProperties.getValidateInputFullName();
        }
        return validText || validFio;
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_FULL_NAME;
    }
}
