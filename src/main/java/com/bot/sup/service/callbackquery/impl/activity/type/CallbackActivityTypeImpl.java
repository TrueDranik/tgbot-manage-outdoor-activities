package com.bot.sup.service.callbackquery.impl.activity.type;

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

import static com.bot.sup.common.enums.CallbackEnum.SUP_ACTIVITY_TYPE;

@Service
@RequiredArgsConstructor
public class CallbackActivityTypeImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;

    private static final CallbackEnum ACTIVITIES = SUP_ACTIVITY_TYPE;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        Long chatId = callbackQuery.getMessage().getChatId();

        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(chatId)
                .text(activityMessageProperties.getActivityType())
                .replyMarkup(setUpKeyboard())
                .build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
// todo IKB builder вынеси в метод
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getListActivityType())
                        .callbackData(CallbackEnum.LIST_ACTIVITY_TYPE.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(activityMessageProperties.getAddActivityType())
                        .callbackData(CallbackEnum.ADD_ACTIVITY_TYPE.toString())
                        .build()));
        buttons.add(List.of(
                InlineKeyboardButton.builder()
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.SUP_ACTIVITIES.toString())
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
