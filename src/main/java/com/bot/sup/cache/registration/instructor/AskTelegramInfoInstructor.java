package com.bot.sup.cache.registration.instructor;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.cache.chain.ChainHandler;
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
public class AskTelegramInfoInstructor extends ChainHandler implements Registration {
    private final UserStateCache userStateCache;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final GetFullNameInstructor getFullNameInstructor;

    private static final InstructorStateEnum STATE = InstructorStateEnum.ASK_TELEGRAM_ID;

    @Override
    public PartialBotApiMethod<?> handleMessage(Message message) {
        PartialBotApiMethod<?> replyToUser;
        Long chatId = message.getChatId();

        UserState userState = new UserState();
        userState.setState(InstructorStateEnum.ASK_TELEGRAM_ID.toString());
        userState.setAdminTelegramId(chatId);

        userStateCache.createOrUpdateState(userState);

        replyToUser = messageService
                .buildReplyMessage(chatId, instructorMessageProperties.getGetTelegramId());

        chainHandle(STATE);

        return replyToUser;
    }

    @Override
    public void chainHandle(Enum<?> state) {
        super.chainHandle(state);
    }

    @Override
    public Enum<?> getState() {
        return STATE;
    }
}
