package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.InstructorStateContext;
import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.model.common.ActivityEnum;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.ActivityEnum.ADD_INSTRUCTOR;

@RequiredArgsConstructor
@Service
public class CallbackAddInstructorImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(ADD_INSTRUCTOR);
    private final InstructorStateContext instructorStateContext;
    private final InstructorDataCache instructorDataCache;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        RegistrationInstructorStateEnum registrationInstructorStateEnum = RegistrationInstructorStateEnum.FILLING_PROFILE;
        instructorDataCache.setInstructorCurrentState(chatId, registrationInstructorStateEnum);

        return instructorStateContext.processInputMessage(registrationInstructorStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
