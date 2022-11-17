package com.bot.sup.service.callbackquery.impl.schedule;

import com.bot.sup.common.enums.CallbackEnum;
import com.bot.sup.common.properties.message.MainMessageProperties;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CallbackScheduleWebAppImpl implements Callback {
    private final MainMessageProperties mainMessageProperties;

    private static final Set<CallbackEnum> ACTIVITIES = Set.of(CallbackEnum.SCHEDULE_WEBAPP);

    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) throws TelegramApiException {
        return EditMessageText.builder()
                .messageId(callbackQuery.getMessage().getMessageId())
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Добавить тур/составить расписание")
                .replyMarkup(createInlineKeyboard())
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard() {
        WebAppInfo webAppInfo = WebAppInfo.builder()
                .url("https://tgsupbot.reliab.tech/admin")
                .build();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Добавить новый маршрут")
                .webApp(new WebAppInfo("https://tgsupbot.reliab.tech/admin/#/route"))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Составить расписание в конструкторе")
                .webApp(new WebAppInfo("https://tgsupbot.reliab.tech/#/admin/constructor"))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Создать новую активность")
                .webApp(new WebAppInfo("https://tgsupbot.reliab.tech/#/admin/activity"))
                .build()));
        buttons.add(List.of(InlineKeyboardButton.builder()
                .text("Изменить существующую активность")
                .webApp(new WebAppInfo("https://tgsupbot.reliab.tech/#/admin/updateActivity"))
                .build()));

        buttons.add(List.of(InlineKeyboardButton.builder()
                .text(mainMessageProperties.getBack())
                .callbackData("SCHEDULE")
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
