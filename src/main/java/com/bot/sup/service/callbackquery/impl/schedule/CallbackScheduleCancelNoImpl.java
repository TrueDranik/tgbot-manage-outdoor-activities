package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CallbackScheduleCancelNoImpl implements Callback {

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_CANCEL_NO);

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        return DeleteMessage.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
