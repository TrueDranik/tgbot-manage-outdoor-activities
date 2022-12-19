package com.bot.sup.service.callbackquery.impl.activity;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.bot.sup.common.enums.CallbackEnum.SUP_ACTIVITIES;

@Service
@RequiredArgsConstructor
public class CallbackActivitiesImpl implements Callback {
    private final ActivityMessageProperties activityMessageProperties;
    private final MainMessageProperties mainMessageProperties;

    public static final CallbackEnum ACTIVITIES = SUP_ACTIVITIES;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
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
                        .text(activityMessageProperties.getActivityFormat())
                        .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getActivityType())
                        .callbackData(CallbackEnum.SUP_ACTIVITY_TYPE.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getMenu())
                        .callbackData(CallbackEnum.MENU.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
