package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.impl.ActivityDataCache;
import com.bot.sup.cache.impl.MiddlewareDataCache;
import com.bot.sup.model.common.CallbackEnum;
//import com.bot.sup.model.common.BotStateEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.bot.sup.model.common.CallbackEnum.ADD_ACTIVITY;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(ADD_ACTIVITY);
    private final StateContext stateContext;
    private final ActivityDataCache activityDataCache;
    private final MiddlewareDataCache middlewareDataCache;


    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        SupActivityStateEnum botStateEnum = SupActivityStateEnum.FILLING_ACTIVITY; //TODO: разделить на разные классы(enum)
        activityDataCache.setActivityCurrentState(chatId, botStateEnum);
        middlewareDataCache.validCurrentState(chatId, activityDataCache);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
