package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.cache.SupActivityDataCache;
import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.SupActivityStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.ADD_ACTIVITY_FORMAT;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityFormatImpl implements Callback {
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(ADD_ACTIVITY_FORMAT);
    private final StateContext stateContext;
    private final SupActivityDataCache supActivityDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        SupActivityStateEnum botStateEnum = SupActivityStateEnum.FILLING_ACTIVITY;
        supActivityDataCache.setActivityCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
