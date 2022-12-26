package com.bot.sup.cache.registration.instructor;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.cache.chain.ChainHandler;
import com.bot.sup.cache.registration.Registration;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

//@Component
@RequiredArgsConstructor
public class GetFullNameInstructor extends ChainHandler implements Registration {
    private final UserStateCache userStateCache;
    private final InstructorRepository instructorRepository;
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;

    private static final InstructorStateEnum STATE = InstructorStateEnum.GET_TELEGRAM_ID;

    @Override
    public PartialBotApiMethod<?> handleMessage(Message message) {
        PartialBotApiMethod<?> replyToUser = null;
        Long chatId = message.getChatId();

        UserState cacheByTelegramId = userStateCache.getByTelegramId(chatId);

        Instructor instructor = new Instructor();
        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        if (forwardFrom.isEmpty()) {
            replyToUser = messageService.buildReplyMessage(chatId, instructorMessageProperties.getTelegramIdIsEmpty());

            return replyToUser;
        }

        instructor.setTelegramId(message.getForwardFrom().getId());
        if (message.getForwardFrom().getUserName() != null) {
            instructor.setUsername(message.getForwardFrom().getUserName());
        }
        instructorRepository.save(instructor);

        cacheByTelegramId.setInstructorTelegramId(instructor.getTelegramId());
        cacheByTelegramId.setState(InstructorStateEnum.GET_TELEGRAM_ID.toString());
        userStateCache.createOrUpdateState(cacheByTelegramId);

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
