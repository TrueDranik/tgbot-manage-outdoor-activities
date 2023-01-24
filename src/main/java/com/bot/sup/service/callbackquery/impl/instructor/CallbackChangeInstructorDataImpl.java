package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.repository.InstructorRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.EntityNotFoundException;

import static com.bot.sup.common.enums.CallbackEnum.CHANGE_INSTRUCTOR;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallbackChangeInstructorDataImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final InstructorRepository instructorRepository;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;
        Instructor instructor = instructorRepository.findByTelegramId(Long.valueOf(instructorId))
                .orElseThrow(() -> new EntityNotFoundException("Instructor with telegramId [%s] not found".formatted(instructorId)));

        UserState userState = new UserState();
        userState.setAdminTelegramId(chatId);
        userState.setState(botStateEnum);
        userState.setEntity(instructor);
        userState.setForUpdate(true);

        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CHANGE_INSTRUCTOR;
    }
}
