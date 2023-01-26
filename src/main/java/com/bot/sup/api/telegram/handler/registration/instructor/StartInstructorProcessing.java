package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartInstructorProcessing implements InstructorMessageProcessor {
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();

        log.info("start instructor registration");

        UserState userState = userStateCache.getByTelegramId(chatId);
        userState.setState(InstructorStateEnum.ASK_FULL_NAME);
        userStateCache.createOrUpdateState(userState);

        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructor());
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return null;
    }

    @Override
    public boolean isMessageInvalid(Message message) {
        return false;
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.START_PROCESSING;
    }
}
