package com.bot.sup.service.callbackquery.impl.activity.type;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.model.entity.ActivityType;
import com.bot.sup.repository.ActivityTypeRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.ACTIVITY_TYPE_OPTION;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeOptionImpl implements Callback {
    private final ActivityTypeRepository activityTypeRepository;
    private final MainMessageProperties mainMessageProperties;

    private static final CallbackEnum ACTIVITIES = ACTIVITY_TYPE_OPTION;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();
        String activityTypeId = callbackQuery.getData().split("/")[1];
        Optional<ActivityType> activityType = activityTypeRepository.findById(Long.parseLong(activityTypeId));

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityType.get().getName())
                .replyMarkup(setUpKeyboard(activityTypeId))
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard(String activityTypeId) {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getChange())
                        .callbackData(CallbackEnum.ACTIVITY_TYPE_CHANGE + "/" + activityTypeId)
                        .build());
        firstRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getDelete())
                        .callbackData(CallbackEnum.DELETE_ACTIVITY_TYPE + "/" + activityTypeId)
                        .build());

        secondRow.add(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_TYPE.toString())
                        .build());

        return InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
