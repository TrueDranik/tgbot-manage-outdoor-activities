package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.impl.InstructorDataCache;
import com.bot.sup.model.common.CallbackEnum;
//import com.bot.sup.model.common.BotStateEnum;
import com.bot.sup.model.common.InstructorStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.CHANGE_INSTRUCTOR;

@RequiredArgsConstructor
@Service
@Slf4j
public class CallbackChangeInstructorDataImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(CHANGE_INSTRUCTOR);
    private final StateContext stateContext;
    private final InstructorDataCache dataCache;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        String instructorId = callbackQuery.getData().split("/")[1];

        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;
        dataCache.saveInstructorForUpdate(callbackQuery.getMessage().getChatId(), Long.parseLong(instructorId));

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
