package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.TelegramProperties;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.common.properties.message.ScheduleMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackScheduleWebAppImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;
    private final ScheduleMessageProperties scheduleMessageProperties;
    private final TelegramProperties telegramProperties;

    @Override
    public PartialBotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text(scheduleMessageProperties.getTourEditor())
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getMakeSchedule())
                .webApp(new WebAppInfo(telegramProperties.getWebAppConstructor()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getCreateActivity())
                .webApp(new WebAppInfo(telegramProperties.getWebAppActivity()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getChangeActivity())
                .webApp(new WebAppInfo(telegramProperties.getWebAppUpdateActivity()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getAddNewRoute())
                .webApp(new WebAppInfo(telegramProperties.getWebAppRoute()))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(scheduleMessageProperties.getChangeDeleteRoute())
                .webApp(new WebAppInfo(telegramProperties.getWebAppUpdateRoute()))
                .build()));

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData(CallbackEnum.MENU.toString())
                .build()));

        return InlineKeyboardMarkup.builder()
                .keyboard(buttons)
                .build();
    }

    @Override
    public CallbackEnum getSupportedActivities() {
        return CallbackEnum.SCHEDULE_WEBAPP;
    }
}
