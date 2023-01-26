package com.bot.sup.service.callbackquery.impl.instructor;

import com.bot.sup.api.telegram.handler.StateContext;
import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.enums.states.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import com.bot.sup.service.callbackquery.Callback;
import com.bot.sup.service.instructor.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.bot.sup.util.UserStateUtil;

import static com.bot.sup.common.enums.CallbackEnum.CHANGE_INSTRUCTOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackChangeInstructorDataImpl implements Callback {
    private final StateContext stateContext;
    private final UserStateCache userStateCache;
    private final InstructorService instructorService;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String instructorId = callbackQuery.getData().split("/")[1];
        InstructorStateEnum botStateEnum = InstructorStateEnum.FILLING_INSTRUCTOR;

        Instructor instructor = instructorService.findByTelegramId(Long.valueOf(instructorId));

        UserState userState = UserStateUtil.getUserState(chatId, botStateEnum, instructor, true);
        userStateCache.createOrUpdateState(userState);

        return stateContext.processInputMessage(botStateEnum, callbackQuery.getMessage());
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CHANGE_INSTRUCTOR;
    }
}
