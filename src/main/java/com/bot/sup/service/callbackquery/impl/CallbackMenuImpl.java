package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.model.common.ActivityEnum;
import com.bot.sup.service.callbackquery.Callback;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.ActivityEnum.MENU;

@Service
public class CallbackMenuImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(MENU);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        HandleMainMenuImpl handleMainMenu = new HandleMainMenuImpl();

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .chatId(callbackQuery.getMessage()
                        .getChatId()
                        .toString())
                .text("Что Вы хотите выбрать?")
                .build();
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}