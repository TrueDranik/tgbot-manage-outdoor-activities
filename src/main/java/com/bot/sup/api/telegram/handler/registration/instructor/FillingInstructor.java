package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.api.telegram.handler.registration.HandleRegistration;
import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.api.telegram.handler.registration.MessageProcessorUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingInstructor implements HandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<InstructorStateEnum, InstructorMessageProcessor> instructorMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        Instructor instructor = (Instructor) userState.getEntity();

        if (userState.getState().equals(InstructorStateEnum.FILLING_INSTRUCTOR)) {
            userState.setState(InstructorStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        return processInputMessage(message, chatId, instructor);
    }

    public BotApiMethod<?> processInputMessage(Message message, Long chatId, Instructor instructor) {
        InstructorStateEnum instructorCurrentState = (InstructorStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = instructorMessageProcessorMap.get(instructorCurrentState);

        return MessageProcessorUtil.messageProcessorUtil(messageProcessor, message, chatId, instructor);
    }

    @Override
    public InstructorStateEnum getType() {
        return InstructorStateEnum.FILLING_INSTRUCTOR;
    }
}
