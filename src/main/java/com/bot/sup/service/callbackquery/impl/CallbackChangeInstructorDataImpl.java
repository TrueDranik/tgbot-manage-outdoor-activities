package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.InstructorStateContext;
import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.model.common.ActivityEnum;
import com.bot.sup.model.common.RegistrationInstructorStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.ActivityEnum.CHANGE_INSTRUCTOR;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallbackChangeInstructorDataImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(CHANGE_INSTRUCTOR);
    private final InstructorStateContext instructorStateContext;
    private final InstructorDataCache dataCache;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String instructorId = callbackQuery.getData().split("/")[1];

        RegistrationInstructorStateEnum registrationInstructorStateEnum = RegistrationInstructorStateEnum.FILLING_PROFILE;
        dataCache.saveInstructorForUpdate(callbackQuery.getMessage().getChatId(), Long.parseLong(instructorId));

        return instructorStateContext.processInputMessage(registrationInstructorStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
