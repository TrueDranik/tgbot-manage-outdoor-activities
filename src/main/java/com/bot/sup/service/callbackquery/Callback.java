package com.bot.sup.service.callbackquery;

import com.bot.sup.common.enums.CallbackEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;

@Component
public interface Callback {
    PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException;

    default boolean isSupportFor(String type) {
        return getSupportedActivities().contains(CallbackEnum.valueOf(type));
    }

    Collection<CallbackEnum> getSupportedActivities();
}
