package com.bot.sup.service.callbackquery;

import com.bot.sup.common.enums.CallbackEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public interface Callback {
    PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException;

    CallbackEnum getSupportedActivities();
}
