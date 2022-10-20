package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.model.common.CallbackEnum;
import com.bot.sup.model.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityFormat;
import com.bot.sup.model.entity.Route;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.model.common.CallbackEnum.ACTIVITY_FORMAT_OPTION;

@RequiredArgsConstructor
@Service
public class CallbackActivityFormatOptionImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    public static final Set<CallbackEnum> ACTIVITIES = Set.of(ACTIVITY_FORMAT_OPTION);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityFormatId = callbackQuery.getData().split("/")[1];
        Optional<ActivityFormat> activityFormat = activityFormatRepository.findById(Long.parseLong(activityFormatId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityFormat.get().getName())
                .replyMarkup(generateKeyboardWithActivity(activityFormatId))
                .build();
    }

    private InlineKeyboardMarkup generateKeyboardWithActivity(String activityId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData("ACTIVITY_FORMAT_CHANGE")
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData("DELETE_ACTIVITY_FORMAT/" + activityId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData("LIST_ACTIVITY_FORMAT")
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public Collection<CallbackEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
