package com.bot.sup.service.callbackquery.impl.activity.format;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.ActivityMessageProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.repository.ActivityFormatRepository;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.bot.sup.common.enums.CallbackEnum.DELETE_ACTIVITY_FORMAT;

@RequiredArgsConstructor
@Service
public class CallbackDeleteActivityFormatImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ActivityMessageProperties activityMessageProperties;
    private final ActivityFormatRepository activityFormatRepository;

    public static final CallbackEnum ACTIVITIES = DELETE_ACTIVITY_FORMAT;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
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
                        .text(mainMessageProperties.getBack())
                        .callbackData(CallbackEnum.SUP_ACTIVITY_FORMAT.toString())
                        .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    //todo подумай над этим метод, если у тебя есть сервис
    private void deleteActivity(Long chatId) {
        activityFormatRepository.deleteById(chatId);
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return ACTIVITIES;
    }
}
