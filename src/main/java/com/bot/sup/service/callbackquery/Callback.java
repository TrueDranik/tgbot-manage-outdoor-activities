package com.bot.sup.service.callbackquery;

import com.bot.sup.model.common.ActivityEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;

public interface Callback {
    BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException;

    default boolean isSupportFor(String type) {
        return getSupportedActivities().contains(ActivityEnum.valueOf(type));
    }

    Collection<ActivityEnum> getSupportedActivities();
}
