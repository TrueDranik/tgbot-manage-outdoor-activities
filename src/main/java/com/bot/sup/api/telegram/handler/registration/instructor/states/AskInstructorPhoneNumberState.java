package com.bot.sup.api.telegram.handler.registration.instructor.states;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InstructorStateEnum;
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
public class AskInstructorPhoneNumberState implements InstructorMessageProcessor {
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();
        String userAnswer = message.getText();

        ((Instructor) instructor).setPhoneNumber(userAnswer);

        log.info("instructor phone number = " + userAnswer);

        userStateCache.getByTelegramId(chatId).setState(InstructorStateEnum.ASK_TELEGRAM_ID);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getGetTelegramId());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getPhoneNumberNotValid());
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return !Validation.isValidPhoneNumber(message.getText());
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_PHONE_NUMBER;
    }
}
