package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ActivityTypeDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeChangeDataImpl implements Callback {
    private final StateContext stateContext;
    private final MiddlewareDataCache middlewareDataCache;
    private final ActivityTypeDataCache activityTypeDataCache;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.ACTIVITY_TYPE_CHANGE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityTypeId = callbackQuery.getData().split("/")[1];

        ActivityTypeStateEnum activityTypeStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;

        activityTypeDataCache.saveActivityTypeForUpdate(chatId, Long.valueOf(activityTypeId));
        middlewareDataCache.setValidCurrentState(chatId, activityTypeStateEnum);

        return stateContext.processInputMessage(activityTypeStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
