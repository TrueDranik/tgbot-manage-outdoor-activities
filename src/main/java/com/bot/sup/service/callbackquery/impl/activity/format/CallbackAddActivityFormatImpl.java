package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityFormatStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_FORMAT;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityFormatImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        ActivityFormatStateEnum botStateEnum = ActivityFormatStateEnum.FILLING_ACTIVITY_FORMAT;
        ActivityFormat activityFormat = new ActivityFormat();

        UserState userState = new UserState();
        userState.setAdminTelegramId(chatId);
        userState.setState(botStateEnum);
        userState.setEntity(activityFormat);
        userState.setForUpdate(false);

        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ADD_ACTIVITY_FORMAT;
    }
}
