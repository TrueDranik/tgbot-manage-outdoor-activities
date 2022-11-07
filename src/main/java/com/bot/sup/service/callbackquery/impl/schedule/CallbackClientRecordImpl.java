package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.ClientRecordDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.ClientRecordStateEnum;
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
public class CallbackClientRecordImpl implements Callback {
    private final StateContext stateContext;
    private final ClientRecordDataCache clientRecordDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.CLIENT_RECORD);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        ClientRecordStateEnum botStateEnum = ClientRecordStateEnum.FILLING_CLIENT;

        clientRecordDataCache.setClientRecrodCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
