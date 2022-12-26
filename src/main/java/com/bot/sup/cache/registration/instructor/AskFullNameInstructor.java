package com.bot.sup.cache.registration.instructor;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.cache.registration.Registration;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

//@Component
@RequiredArgsConstructor
public class AskFullNameInstructor implements Registration {
    private final UserStateCache userStateCache;
    private final InstructorMessageProperties instructorMessageProperties;
    private final MessageService messageService;

    private static final InstructorStateEnum STATE = InstructorStateEnum.ASK_FULL_NAME;

    @Override
    public PartialBotApiMethod<?> handleMessage(Message message) {
        PartialBotApiMethod<?> replyToUser;
        Long chatId = message.getChatId();

        UserState userState = new UserState();
        userState.setState(STATE.toString());

        userStateCache.createOrUpdateState(userState);

        replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getInputFullNameInstructor());



        return replyToUser;
    }

    @Override
    public Enum<?> getState() {
        return STATE;
    }
}
