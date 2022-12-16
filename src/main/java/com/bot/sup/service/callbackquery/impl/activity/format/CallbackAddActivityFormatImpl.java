package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ActivityFormatDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_FORMAT;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityFormatImpl implements Callback {
    private final StateContext stateContext;
    private final ActivityFormatDataCache activityFormatDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    public static final CallbackEnum ACTIVITIES = ADD_ACTIVITY_FORMAT;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        middlewareDataCache.removeCurrentState(chatId);
        ActivityFormatStateEnum botStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
        activityFormatDataCache.setActivityFormatCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
