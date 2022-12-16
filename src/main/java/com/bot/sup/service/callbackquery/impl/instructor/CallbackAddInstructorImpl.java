package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.InstructorDataCache;
import com.bot.sup.cache.MiddlewareDataCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.InstructorStateEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.bot.sup.common.enums.CallbackEnum.ADD_INSTRUCTOR;

@RequiredArgsConstructor
@Service
public class CallbackAddInstructorImpl implements Callback {
    private final StateContext stateContext;
    private final InstructorDataCache instructorDataCache;
    private final MiddlewareDataCache middlewareDataCache;

    public static final CallbackEnum ACTIVITIES = ADD_INSTRUCTOR;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;
        instructorDataCache.setInstructorCurrentState(chatId, botStateEnum);
        middlewareDataCache.setValidCurrentState(chatId, botStateEnum);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
