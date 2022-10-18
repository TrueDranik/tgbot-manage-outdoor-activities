package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.ActivityMessageProperties;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.repository.RouteRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.model.common.CallbackEnum.DELETE_ACTIVITY_FORMAT;

@RequiredArgsConstructor
@Service
public class CallbackDeleteActivityFormatImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final RouteRepository routeRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(DELETE_ACTIVITY_FORMAT);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityId = callbackQuery.getData().split("/")[1];
        deleteActivity(Long.parseLong(activityId));

        return EditMessageText.builder().messageId(callbackQuery.getMessage().getMessageId())
                .text(activityMessageProperties.getDeleteActivity())
                .chatId(chatId)
                .replyMarkup(createKeyboardForDeleteActivity())
                .build();
    }

    private InlineKeyboardMarkup createKeyboardForDeleteActivity() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getMenu())
                        .callbackData("MENU")
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    private void deleteActivity(Long chatId) {
        routeRepository.deleteById(chatId);
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
