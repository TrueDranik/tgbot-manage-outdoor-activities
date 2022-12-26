package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class AskFullName implements InstructorRegistration {
    private final InstructorDataCache instructorDataCache;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Instructor instructor) {
        Long chatId = message.getChatId();
        BotApiMethod<?> replyToUser;

        replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructor());
        instructorDataCache.setInstructorCurrentState(chatId, InstructorStateEnum.ASK_PHONE_NUMBER);

        return replyToUser;
    }

    @Override
    public boolean support(InstructorStateEnum instructorCurrentState) {
        return InstructorStateEnum.ASK_FULL_NAME.equals(instructorCurrentState);
    }
}
