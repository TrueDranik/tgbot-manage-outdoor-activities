package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.activity.format.impl.ActivityFormatServiceImpl;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.bot.sup.util.UserStateUtil;

@Service
@RequiredArgsConstructor
public class CallbackActivityFormatChangeDataImpl implements Callback {
    private final StateContext stateContext;
    private final ActivityFormatServiceImpl activityFormatService;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        ActivityFormatStateEnum activityFormatStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;

        ActivityFormat activityFormat = activityFormatService.findActivityFormatById(Long.valueOf(activityFormatId));

        UserState userState = UserStateUtil.getUserState(chatId, activityFormatStateEnum, activityFormat, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(activityFormatStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITY_FORMAT_CHANGE;
    }
}
