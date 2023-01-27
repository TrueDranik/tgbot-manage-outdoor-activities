package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import com.bot.sup.api.telegram.handler.registration.instructor.states.InstructorMessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.util.MessageProcessorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FillingInstructor implements InstructorHandleRegistration {
    private final UserStateCache userStateCache;
    private final Map<InstructorStateEnum, InstructorMessageProcessor> instructorMessageProcessorMap;

    @Override
    public BotApiMethod<?> resolveState(Message message) {
        Long chatId = message.getChatId();
        UserState userState = userStateCache.getByTelegramId(chatId);
        Instructor instructor = (Instructor) userState.getEntity();

        if (userState.getState().equals(InstructorStateEnum.FILLING_INSTRUCTOR)) {
            userState.setState(InstructorStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        InstructorStateEnum instructorCurrentState = (InstructorStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = instructorMessageProcessorMap.get(instructorCurrentState);

        return MessageProcessorUtil.messageProcessorCheck(messageProcessor, message, chatId, instructor);
    }

    @Override
    public InstructorStateEnum getType() {
        return InstructorStateEnum.FILLING_INSTRUCTOR;
    }
}
