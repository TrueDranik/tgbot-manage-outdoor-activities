package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ActivityTypeDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityTypeImpl implements Callback {
    private final StateContext stateContext;
    private final ActivityTypeDataCache activityTypeDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    public static final CallbackEnum ACTIVITIES = ADD_ACTIVITY_TYPE;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        ActivityTypeStateEnum botStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
        activityTypeDataCache.setActivityTypeCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
