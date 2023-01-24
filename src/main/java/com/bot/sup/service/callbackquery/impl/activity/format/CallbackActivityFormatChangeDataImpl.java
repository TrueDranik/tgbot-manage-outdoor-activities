package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class CallbackActivityFormatChangeDataImpl implements Callback {
    private final StateContext stateContext;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];

        ActivityFormatStateEnum activityFormatStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;

        // TODO: 23.01.2023 Сделать обновление сущности

        return stateContext.processInputMessage(activityFormatStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITY_FORMAT_CHANGE;
    }
}
