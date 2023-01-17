package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.AboutUsDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class CallbackAddAboutUsInfoImpl implements Callback {
    private final StateContext stateContext;
    private final AboutUsDataCache aboutUsDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    public static final CallbackEnum ACTIVITIES = CallbackEnum.ADD_ABOUT_US_INFO;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        middlewareDataCache.removeCurrentState(chatId);
        AboutUsStateEnum currentState = AboutUsStateEnum.FILLING_ABOUT_US;
        aboutUsDataCache.setAboutUsCurrentState(chatId, currentState);
        middlewareDataCache.setValidCurrentState(chatId, currentState);

        return stateContext.processInputMessage(currentState, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
