package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.MenuMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.MENU;

@Service
@RequiredArgsConstructor
public class CallbackMenuImpl implements Callback {
    private final MenuMessageProperties menuMessageProperties;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(MENU);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        HandleMainMenuImpl handleMainMenu = new HandleMainMenuImpl();

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .chatId(callbackQuery.getMessage()
                        .getChatId()
                        .toString())
                .text(menuMessageProperties.getUserChoose())
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}