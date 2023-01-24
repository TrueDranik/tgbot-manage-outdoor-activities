package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.ActivityTypeStateEnum;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.service.activity.type.impl.ActivityTypeServiceImpl;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.UserStateUtil;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeChangeDataImpl implements Callback {
    private final StateContext stateContext;
    private final ActivityTypeServiceImpl activityTypeService;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityTypeId = callbackQuery.getData().split("/")[1];
        ActivityTypeStateEnum activityTypeStateEnum = ActivityTypeStateEnum.FILLING_ACTIVITY_TYPE;

        ActivityType activityType = activityTypeService.findActivityTypeById(Long.valueOf(activityTypeId));

        UserState userState = UserStateUtil.getUserState(chatId, activityTypeStateEnum, activityType, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(activityTypeStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.ACTIVITY_TYPE_CHANGE;
    }
}
