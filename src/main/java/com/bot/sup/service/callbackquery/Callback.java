package com.bot.sup.service.callbackquery;

import com.bot.sup.enums.ActivityEnum;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Collection;

public interface Callback {
    BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery);

    default boolean isSupportFor(String type) {
        return getSupportedActivities().contains(ActivityEnum.valueOf(type));
    }

    Collection<ActivityEnum> getSupportedActivities();
}
