package com.bot.sup.api.telegram.handler.registration;

import com.bot.sup.api.telegram.handler.registration.instructor.InstructorMessageProcessor;
import com.bot.sup.api.telegram.handler.registration.instructor.MessageProcessor;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.instructor.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FillingInstructor implements HandleRegistration {
    private final InstructorService instructorService;
    private final InstructorRepository instructorRepository;
    private final UserStateCache userStateCache;
    private final Map<InstructorStateEnum, InstructorMessageProcessor> instructorMessageProcessorMap;

    @Override
    public BotApiMethod<?> getMessage(Message message) {
        Instructor instructor;
        Long chatId = message.getChatId();
        Long instructorForUpdateId = 0L;
        UserState userState = userStateCache.getByTelegramId(chatId);

        boolean forUpdate = userState.isForUpdate();
        if (forUpdate) {
            instructor = instructorRepository.findByTelegramId(instructorForUpdateId)
                    .orElseThrow(EntityNotFoundException::new);

            log.info("Found instructor with tgId - {} and name - {}", instructor.getTelegramId(), instructor.getFirstName());
        } else {
            instructor = (Instructor) userState.getEntity();
        }

        if (userState.getState().equals(InstructorStateEnum.FILLING_INSTRUCTOR)) {
            userState.setState(InstructorStateEnum.START_PROCESSING);
            userStateCache.createOrUpdateState(userState);
        }

        return processInputMessage(message, chatId, instructor, forUpdate);
    }

    @Transactional
    public BotApiMethod<?> processInputMessage(Message inputMessage, Long chatId, Instructor instructor, boolean forUpdate) {
        InstructorStateEnum instructorCurrentState = (InstructorStateEnum) userStateCache.getByTelegramId(chatId).getState();
        MessageProcessor messageProcessor = instructorMessageProcessorMap.get(instructorCurrentState);

        if (messageProcessor.isMessageInvalid(inputMessage)) {
            return messageProcessor.processInvalidInputMessage(inputMessage.getChatId());
        }

        if (forUpdate) {
            instructorService.save(instructor);
        } else {
            userStateCache.getByTelegramId(chatId).setEntity(instructor);
        }

        return messageProcessor.processInputMessage(inputMessage, instructor);
    }

    @Override
    public InstructorStateEnum getType() {
        return InstructorStateEnum.FILLING_INSTRUCTOR;
    }
}
