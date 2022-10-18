package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.model.common.CallbackEnum.SUP_ACTIVITIES;

@Service
@RequiredArgsConstructor
public class CallbackActivitiesImpl implements Callback {
    private final ActivityMessageProperties activityMessageProperties;
    private final MainMessageProperties mainMessageProperties;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(SUP_ACTIVITIES);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityMessageProperties.getActivities())
                .replyMarkup(inlineKeyboardMarkup())
                .build();
    }

    private InlineKeyboardMarkup inlineKeyboardMarkup() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getListActivityFormat())
                        .callbackData("SUP_ACTIVITY_FORMAT")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getListActivityType())
                        .callbackData("SUP_ACTIVITY_TYPE")
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getMenu())
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
