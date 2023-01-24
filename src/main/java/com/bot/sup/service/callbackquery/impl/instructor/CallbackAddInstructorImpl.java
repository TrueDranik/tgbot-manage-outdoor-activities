package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import util.UserStateUtil;

import static com.bot.sup.common.enums.CallbackEnum.ADD_INSTRUCTOR;

@Service
@RequiredArgsConstructor
public class CallbackAddInstructorImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;
        Instructor instructor = new Instructor();

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, instructor, false);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ADD_INSTRUCTOR;
    }
}
