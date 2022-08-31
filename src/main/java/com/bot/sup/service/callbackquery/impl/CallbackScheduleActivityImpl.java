package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.enums.ActivityEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.enums.ActivityEnum.SCHEDULE_ACTIVITY;

@RequiredArgsConstructor
@Service
public class CallbackScheduleActivityImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVTIES = Set.of(SCHEDULE_ACTIVITY);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVTIES;
    }
}
