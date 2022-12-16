package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ClientRecordDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class CallbackClientRecordImpl implements Callback {
    private final StateContext stateContext;
    private final ClientRecordDataCache clientRecordDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    private static final CallbackEnum ACTIVITIES = CallbackEnum.CLIENT_RECORD;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String scheduleId = callbackQuery.getData().split("/")[1];
        ClientRecordStateEnum botStateEnum = ClientRecordStateEnum.FILLING_CLIENT;

        clientRecordDataCache.setScheduleState(chatId, Long.valueOf(scheduleId));
        clientRecordDataCache.setClientRecordCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
