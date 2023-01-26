package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.bot.sup.util.UserStateUtil;

import static com.bot.sup.common.enums.CallbackEnum.ADD_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackAddActivityTypeImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        ActivityTypeStateEnum botStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;
        ActivityType activityType = new ActivityType();

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, activityType, false);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ADD_ACTIVITY_TYPE;
    }
}
