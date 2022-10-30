package com.bot.sup.service.callbackquery.impl.activity;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.cache.ActivityFormatDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_FORMAT;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityFormatImpl implements Callback {
    private final StateContext stateContext;
    private final ActivityFormatDataCache activityFormatDataCache;
    private final MiddlewareDataCache middlewareDataCache;
    
    public static final Set<CallbackEnum> ACTIVITIES = Set.of(ADD_ACTIVITY_FORMAT);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        ActivityFormatStateEnum botStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
        activityFormatDataCache.setActivityFormatCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
