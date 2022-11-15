package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ActivityFormatDataCache;
import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
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
public class CallbackActivityFormatChangeDataImpl implements Callback {
    private final StateContext stateContext;
    private final MiddlewareDataCache middlewareDataCache;
    private final ActivityFormatDataCache activityFormatDataCache;


    public static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.ACTIVITY_FORMAT_CHANGE);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        ActivityFormatStateEnum activityFormatStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;

        activityFormatDataCache.saveActivityFormatForUpdate(chatId, Long.valueOf(activityFormatId));
        middlewareDataCache.setValidCurrentState(chatId, activityFormatStateEnum);

        return stateContext.processInputMessage(activityFormatStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
