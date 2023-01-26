package com.bot.sup.api.telegram.handler.registration.instructor;

import com.bot.sup.api.telegram.handler.registration.KeyboardUtil;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.InstructorStateEnum;
import com.bot.sup.common.properties.message.InstructorMessageProperties;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.MessageService;
import com.bot.sup.service.instructor.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AskInstructorTelegramId implements InstructorMessageProcessor {
    private final MessageService messageService;
    private final InstructorMessageProperties instructorMessageProperties;
    private final InstructorService instructorService;
    private final UserStateCache userStateCache;

    @Override
    public BotApiMethod<?> processInputMessage(Message message, Object instructor) {
        Long chatId = message.getChatId();

        ((Instructor) instructor).setTelegramId(message.getForwardFrom().getId());
        if (message.getForwardFrom().getUserName() != null) {
            ((Instructor) instructor).setUsername(message.getForwardFrom().getUserName());
        }

        log.info("instructor TelegramId = " + message.getForwardFrom().getId());
        log.info("User name = " + message.getForwardFrom().getUserName());

        instructorService.save((Instructor) instructor);

        InlineKeyboardMarkup keyboardMarkup = KeyboardUtil
                .keyboardMarkup(CallbackEnum.INSTRUCTORS.toString(), instructorMessageProperties.getMenuInstructors());

        return messageService.getReplyMessageWithKeyboard(chatId, instructorMessageProperties.getRegistrationDone() +
                instructorInfo(((Instructor) instructor)), keyboardMarkup);
    }

    @Override
    public BotApiMethod<?> processInvalidInputMessage(Long chatId) {
        return messageService.buildReplyMessage(chatId, instructorMessageProperties.getTelegramIdAlreadyTaken());
    }

    @Cacheable("states")
    @Override
    public boolean isMessageInvalid(Message message) {
        Optional<User> forwardFrom = Optional.ofNullable(message.getForwardFrom());
        return (forwardFrom.isPresent()
                && instructorService.existsByTelegramId(forwardFrom.get().getId())
                && !userStateCache.getByTelegramId(message.getChatId()).isForUpdate());
    }

    @Override
    public InstructorStateEnum getCurrentState() {
        return InstructorStateEnum.ASK_TELEGRAM_ID;
    }

    private String instructorInfo(Instructor instructor) {
        return "ФИ: " + instructor.getFirstName() + " " + instructor.getLastName()
                + "\nНомер телефона: " + instructor.getPhoneNumber()
                + "\nИмя пользователя: @" + instructor.getUsername();
    }
}
