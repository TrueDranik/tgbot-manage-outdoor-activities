package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.impl.HandleMainMenuImpl;
import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Collection;
import java.util.Set;

import static com.bot.sup.common.enums.CallbackEnum.MENU;

@Service
@RequiredArgsConstructor
public class CallbackMenuImpl implements Callback {
    private final HandleMainMenuImpl handleMainMenu;
    private final MainMessageProperties mainMessageProperties;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(MENU);

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .replyMarkup(handleMainMenu.createInlineKeyboard())
                .chatId(callbackQuery.getMessage().getChatId().toString())
                .text(mainMessageProperties.getUserChoose())
                .parseMode("Markdown")
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}