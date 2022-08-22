package com.bot.sup.service.callbackquery.impl;

import com.bot.sup.api.telegram.handler.impl.HandleInfoImpl;
import com.bot.sup.service.callbackquery.ActivityEnum;
import com.bot.sup.service.callbackquery.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bot.sup.service.callbackquery.ActivityEnum.SCHEDULE;

@RequiredArgsConstructor
@Service
public class CallbackMainMenuImpl implements Callback {
    public static final Set<ActivityEnum> ACTIVITIES = Set.of(SCHEDULE);


    @Override
    public BotApiMethod<?> getCallbackQuery(CallbackQuery callbackQuery) {
        HandleInfoImpl handleInfo = new HandleInfoImpl();
        return SendMessage.builder().chatId(callbackQuery.getMessage().getChatId()).replyMarkup(replyKeyboard() ).text("HUE").build();
    }

    private InlineKeyboardMarkup setUpKeyboard() {
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> Buttons = new ArrayList<>();

        InlineKeyboardButton schedule = new InlineKeyboardButton("Привет");
        InlineKeyboardButton instructors = new InlineKeyboardButton("Инструкторы");
        InlineKeyboardButton activities = new InlineKeyboardButton("Активности");

        keyboard.add(Buttons);
        Buttons.add(schedule);
        Buttons.add(instructors);
        Buttons.add(activities);


        schedule.setCallbackData("SCHEDULE");
        instructors.setCallbackData("INSTRUCTORS");
        activities.setCallbackData("ACTIVITIES");

        firstRow.add(schedule);
        secondRow.add(instructors);
        thirdRow.add(activities);

        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder()
                .keyboardRow(firstRow)
                .keyboardRow(secondRow)
                .keyboardRow(thirdRow)
                .build();

        return build;
    }

    private ReplyKeyboardMarkup replyKeyboard() {


        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(KeyboardButton.builder().text("asdassd").build());

        return ReplyKeyboardMarkup.builder().keyboardRow(keyboardRow).build();

    }

    @Override
    public boolean isSupportFor(String type) {
        return getSupportedActivities().contains(ActivityEnum.valueOf(type));
    }

    @Override
    public Collection<ActivityEnum> getSupportedActivities() {
        return ACTIVITIES;
    }
}
